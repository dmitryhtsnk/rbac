pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-boot-app"
    }

    stages {
        stage('Check tools') {
            steps {
                echo '🔍 Перевірка наявності docker і kubectl'
                sh 'which docker || echo "❌ docker not found"'
                sh 'docker --version || echo "❌ docker not working"'
                sh 'which kubectl || echo "❌ kubectl not found"'
                sh 'kubectl version --client || echo "❌ kubectl not working"'
            }
        }

        stage('Checkout') {
            steps {
                echo '📥 Завантаження коду'
                git url: 'https://github.com/dmitryhtsnk/rbac.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                echo '⚙️ Збірка проєкту'
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Запуск тестів'
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                echo '📦 Створення jar'
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo '🗃 Архівація артефактів'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Створення Docker-образу ${IMAGE_NAME}"
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Load Image to Minikube') {
            steps {
                echo "🚚 Завантаження образу в Minikube"
                sh 'minikube image load ${IMAGE_NAME}'
            }
        }

        stage('Deploy to Minikube') {
            steps {
                echo "🚀 Деплой у Minikube"
                sh 'kubectl apply -f k8s/'
            }
        }
    }

    post {
        success {
            echo '✅ Успішна збірка!'
        }
        failure {
            echo '❌ Помилка під час збірки.'
        }
    }
}
