cat << 'EOF' > Jenkinsfile
pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-boot-app"
    }

    stages {
        stage('Check tools') {
            steps {
                echo '🔍 Перевірка наявності docker і kubectl'
                sh 'echo "DOCKER:"'
                sh 'which docker || echo "❌ docker not found"'
                sh 'docker --version || echo "❌ docker not working"'

                sh 'echo "KUBECTL:"'
                sh 'which kubectl || echo "❌ kubectl not found"'
                sh 'kubectl version --client || echo "❌ kubectl not working"'
            }
        }

        stage('Checkout') {
            steps {
                echo '📥 Завантаження коду з репозиторію'
                git url: 'https://github.com/dmitryhtsnk/rbac.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                echo '⚙️ Компіляція коду'
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
                echo '📦 Створення JAR'
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo '🗃 Архівація JAR'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Збірка Docker-образу з імʼям ${IMAGE_NAME}"
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Load Image to Minikube') {
            steps {
                echo "🚚 Завантаження Docker-образу до Minikube"
                sh 'minikube image load ${IMAGE_NAME}'
            }
        }

        stage('Deploy to Minikube') {
            steps {
                echo "🚀 Деплой в Minikube"
                sh 'kubectl apply -f k8s/'
            }
        }
    }

    post {
        success {
            echo '✅ Збірка та деплой успішні!'
        }
        failure {
            echo '❌ Сталася помилка під час виконання pipeline.'
        }
    }
}
EOF


