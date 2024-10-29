// vars/simpleCalculatorPipeline.groovy
def call() {
    pipeline {
        agent any
        environment {
            DOCKER_IMAGE = 'web-calculator:latest'
            REPO_URL = 'https://github.com/RaamHorakeri/simple-calculator.git'
        }
        stages {
            stage('Checkout') {
                steps {
                    git branch: 'main', url: REPO_URL
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        try {
                            // Ensure Docker Pipeline Plugin is installed or use `sh` command instead
                            docker.build(DOCKER_IMAGE)
                        } catch (Exception e) {
                            echo "Docker build failed: ${e.getMessage()}"
                            throw e
                        }
                    }
                }
            }

            // Uncomment and configure as needed to run tests
            // stage('Run Tests') {
            //     steps {
            //         script {
            //             sh 'docker-compose run --rm app npm test'
            //         }
            //     }
            // }

            // Uncomment if pushing the image to a registry
            // stage('Push Docker Image') {
            //     steps {
            //         script {
            //             docker.image(DOCKER_IMAGE).push()
            //         }
            //     }
            // }

            stage('Deploy') {
                steps {
                    script {
                        sh 'docker compose up -d'
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
