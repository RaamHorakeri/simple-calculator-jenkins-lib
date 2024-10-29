// vars/simpleCalculatorPipeline.groovy
def call() {
    pipeline {
        agent any
        environment {
            DOCKER_IMAGE = 'calculator:latest'
            REPO_URL = 'https://github.com/RaamHorakeri/simple-calculator.git'
        }
        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

stage('Build Docker Image') {
    steps {
        script {
            try {
                docker.build("${DOCKER_IMAGE}")
            } catch (Exception e) {
                echo "Docker build failed: ${e.getMessage()}"
                throw e
            }
        }
    }
}


            // stage('Run Tests') {
            //     steps {
            //         script {
            //             // Assuming tests are defined, e.g., in a separate test script
            //             sh 'docker-compose run --rm app npm test'
            //         }
            //     }
            // }

            // stage('Push Docker Image') {
            //     steps {
            //         script {
            //             // Push the Docker image to a repository (optional)
            //             // docker.image("${DOCKER_IMAGE}").push()
            //         }
            //     }
            // }

            stage('Deploy') {
                steps {
                    script {
                        sh 'docker-compose up -d'
                    }
                }
            }
        }
        post {
            always {
                cleanWs()
            }
            failure {
                echo "Pipeline failed!"
            }
            success {
                echo "Pipeline succeeded!"
            }
        }
    }
}
