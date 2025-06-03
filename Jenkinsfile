pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-boot-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/dmitryhtsnk/rbac.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Потрібно попередньо в Jenkins виконати: eval $(minikube docker-env)
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                    echo "Docker image ${IMAGE_NAME}:latest built."
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    // Упевнись, що kubectl налаштований на minikube (kubectl config use-context minikube)
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'

                    // Очікування, поки деплой завершиться
                    sh 'kubectl rollout status deployment/spring-boot-app --watch=true'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline successfully completed and deployed to Minikube!'
        }
        failure {
            echo '❌ Build or Deployment failed!'
        }
    }
}
