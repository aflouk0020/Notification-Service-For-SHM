pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw clean verify'
                    } else {
                        bat 'mvnw.cmd clean verify'
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: 'target/site/jacoco/jacoco.xml']],
                        id: 'notification-service-coverage',
                        name: 'Notification Service Coverage'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        if (isUnix()) {
                            sh './mvnw sonar:sonar -Dsonar.projectKey=notification-service -Dsonar.projectName="Notification Service"'
                        } else {
                            bat 'mvnw.cmd sonar:sonar -Dsonar.projectKey=notification-service -Dsonar.projectName="Notification Service"'
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    post {
        always {
            echo 'Notification Service pipeline finished.'
        }
        success {
            echo 'Notification Service build successful.'
        }
        failure {
            echo 'Notification Service pipeline failed.'
        }
    }
}
