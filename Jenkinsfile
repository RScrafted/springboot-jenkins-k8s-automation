pipeline {
    agent {
        label 'jenkins_agent1'
    }

    // Using Maven via Global Configuration
    tools {
        maven 'Maven-3.9'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }

    environment {
        // Docker Registry Username is configured under global Environment.
        // DOCKER_REGISTRY_USER = 'username'
        ENV                  = 'prodn'
        IMAGE_NAME           = 'rs-inventory-app'
        IMAGE_TAG            = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile & Test') {
            steps {
                sh 'mvn clean compile test'
            }
        }

        stage('Build & Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_REGISTRY_USER}/${IMAGE_NAME}-${ENV}:${IMAGE_TAG} ."
                sh "docker tag ${DOCKER_REGISTRY_USER}/${IMAGE_NAME}-${ENV}:${IMAGE_TAG} ${DOCKER_REGISTRY_USER}/${IMAGE_NAME}-${ENV}:latest"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', passwordVariable: 'DH_PASS', usernameVariable: 'DH_USER')]) {
                    sh """
                        echo "\$DH_PASS" | docker login -u "\$DH_USER" --password-stdin
                        docker push ${DOCKER_REGISTRY_USER}/${IMAGE_NAME}-${ENV}:${IMAGE_TAG}
                        docker push ${DOCKER_REGISTRY_USER}/${IMAGE_NAME}-${ENV}:latest
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def fullImage = "${env.DOCKER_REGISTRY_USER}/${env.IMAGE_NAME}-${env.ENV}:${env.IMAGE_TAG}"

                    sh """
                        git checkout k8s/deployment.yaml
                        sed -i "s|IMAGE_PLACEHOLDER|${fullImage}|g" k8s/deployment.yaml
                        kubectl apply -f k8s/deployment.yaml
                        kubectl apply -f k8s/service.yaml
                        kubectl rollout status deployment/${env.IMAGE_NAME} --timeout=60s
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline has finished.'
            sh 'docker logout || true' // If logout fails, exits with `0`.
        }
        success {
            echo "Successfully deployed build ${IMAGE_TAG} to local k3s cluster!"
        }
        failure {
            echo 'Build failed.'
        }
    }
}