pipeline {
    agent any
//    agent {
//        docker {
//              image 'eclipse-temurin:17.0.9_9-jdk-jammy'
//              args '--network host -u root -v /var/run/docker.sock:/var/run/docker.sock'
//        }
//

    stages {
        stage('Build') { 
            steps {
                withGradle {
                    sh './gradlew build -x test'
                }
            }
        }
        stage('Test') { 
            steps {
                withGradle {
                    sh './gradlew test'
                }
            }
        }
        stage('Deploy') { 
            steps {
                echo "Deploying" 
            }
        }
    }
}
