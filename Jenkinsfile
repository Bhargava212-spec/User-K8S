
pipeline {
    agent any

    environment {
        IMAGE_NAME = "user-k8s"
        IMAGE_TAG = "v1"
        DEPLOYMENT_FILE = "deployment.yaml"
        COMPOSE_FILE = "docker-compose.yml"
        COMPOSE_PROJECT_NAME = 'user-k8s'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Bhargava212-spec/User-K8S',
                    credentialsId: 'github-creds'
            }
        }



        stage('Start Services via Compose') {
            steps {
                script {
                        // Print which compose file Jenkins sees
                        bat 'echo Using %COMPOSE_FILE%'
                        // Compose v2 command
                        bat 'docker compose -f %COMPOSE_FILE% up -d --remove-orphans'
                        // Optional: list services
                        bat 'docker compose -f %COMPOSE_FILE% ps'
                    }
            }
        }




        stage('Build JAR') {
            steps {
                script {
                    echo "Building Spring Boot JAR..."
                    bat './mvnw clean package -DskipTests'
                }
            }
        }


        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
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
