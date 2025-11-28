
pipeline {
    agent any

    environment {
        IMAGE_NAME = "user-k8s"
        IMAGE_TAG = "v1"
        DEPLOYMENT_FILE = "deployment.yaml"
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Bhargava212-spec/User-K8S',
                    credentialsId: 'github-creds'
            }
        }

        stage('Start Infra Services') {
            steps {
                script {
                    echo "Starting MySQL and Kafka using Docker Compose..."
                    sh "docker-compose -f ${COMPOSE_FILE} up -d mysql zookeeper kafka"
                }
            }
        }


        stage('Build JAR') {
            steps {
                script {
                    echo "Building Spring Boot JAR..."
                    sh './mvnw clean package -DskipTests'
                }
            }
        }


        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl apply -f ${DEPLOYMENT_FILE}"
            }
        }

        stage('Cleanup Infra') {
            steps {
                script {
                    echo "Stopping MySQL and Kafka containers..."
                    sh "docker-compose -f ${COMPOSE_FILE} down"
                }
            }
        }
    }

    post {
        success {
            echo "Deployment successful!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
