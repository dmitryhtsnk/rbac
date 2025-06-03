// pipeline {
//     agent any
//
//     environment {
//         IMAGE_NAME = "spring-boot-app:latest"
//     }
//
//     stages {
//         stage('Checkout') {
//             steps {
//                 git url: 'https://github.com/dmitryhtsnk/rbac.git', branch: 'main'
//             }
//         }
//
//         stage('Build') {
//             steps {
//                 sh './mvnw clean compile'
//             }
//         }
//
//         stage('Test') {
//             steps {
//                 sh './mvnw test'
//             }
//         }
//
//         stage('Package') {
//             steps {
//                 sh './mvnw package -DskipTests'
//             }
//         }
//
//         stage('Archive Artifacts') {
//             steps {
//                 archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
//             }
//         }
//
//         stage('Build Docker Image') {
//             steps {
//                 sh 'docker build -t $IMAGE_NAME .'
//                 sh 'docker image ls'
//             }
//         }
//
//         stage('Deploy to Minikube') {
//             steps {
//                 sh 'kubectl apply -f k8s/deployment.yaml'
//                 sh 'kubectl apply -f k8s/service.yaml'
//                 sh 'kubectl rollout status deployment/spring-boot-app --watch=true'
//             }
//         }
//     }
//
//     post {
//         success {
//             echo '✅ Pipeline successfully completed!'
//         }
//         failure {
//             echo '❌ Build failed!'
//         }
//     }
// }

pipeline {
    agent any
    stages {
        stage('Check tools') {
            steps {
                sh 'echo "DOCKER:"'
                sh 'which docker || echo "❌ docker not found"'
                sh 'docker --version || echo "❌ docker not working"'

                sh 'echo "KUBECTL:"'
                sh 'which kubectl || echo "❌ kubectl not found"'
                sh 'kubectl version --client || echo "❌ kubectl not working"'
            }
        }
    }
}
