
pipeline {

  agent {
    // Uses the official image that includes docker-compose CLI
    docker {
      image 'docker/compose:latest'
      // Mount the host Docker socket so compose can control Docker on the host
      args '-v /var/run/docker.sock:/var/run/docker.sock -v $WORKSPACE:$WORKSPACE -w $WORKSPACE'
    }
  }

  options {
    timestamps()
    ansiColor('xterm')
    buildDiscarder(logRotator(numToKeepStr: '20'))
    timeout(time: 30, unit: 'MINUTES')
  }


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
