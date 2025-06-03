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
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
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
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                    echo "✅ Docker image ${IMAGE_NAME}:latest built."
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'
                    timeout(time: 5, unit: 'MINUTES') {
                        sh 'kubectl rollout status deployment/spring-boot-app --watch=true'
                    }
                    echo "🚀 Application deployed to Minikube."
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline successfully completed!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
