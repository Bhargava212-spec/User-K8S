
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
        sh '''
          set -eux
          # Run docker/compose image to execute compose commands, mounting workspace + docker socket
          docker run --rm \
            -v /var/run/docker.sock:/var/run/docker.sock \
            -v "$PWD":"$PWD" -w "$PWD" \
            docker/compose:latest \
            sh -lc "docker compose -f '$COMPOSE_FILE' -p '$COMPOSE_PROJECT_NAME' up -d mysql zookeeper kafka || docker-compose -f '$COMPOSE_FILE' -p '$COMPOSE_PROJECT_NAME' up -d mysql zookeeper kafka"
        '''
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
