
pipeline {
    agent any

    environment {
        IMAGE_NAME = "user-k8s"
        IMAGE_TAG = "v1"
        DEPLOYMENT_FILE = "deployment.yaml"
        COMPOSE_FILE = "docker-compose.yml"
        COMPOSE_PROJECT_NAME = 'user-k8s'
    }

   tools {
    maven 'Maven-3.9.11'
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
                        bat 'echo Using %COMPOSE_FILE%'
                        bat 'docker compose -f %COMPOSE_FILE% up -d --remove-orphans'
                        bat 'docker compose -f %COMPOSE_FILE% ps'
                    }
            }
        }




        stage('Build JAR') {
            steps {
                script {
                    echo "Building Spring Boot JAR..."
                   bat 'mvn -version'
                   bat 'mvn -Dmaven.test.failure.ignore=false clean package'

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
                bat "kubectl apply -f ${DEPLOYMENT_FILE}"
            }
        }

        stage('Cleanup Infra') {
            steps {
                script {
                    echo "Stopping MySQL and Kafka containers..."
                    bat "docker-compose -f ${COMPOSE_FILE} down"
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
