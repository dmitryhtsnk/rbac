pipeline {
    agent any

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
                    sh 'eval $(minikube docker-env)' // важливо!
                    sh 'docker build -t spring-boot-app:latest .'
                }
            }
        }

        stage('Deploy to Minikube') {
            steps {
                script {
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'
                    sh 'kubectl rollout status deployment/spring-boot-app --watch=true'
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
