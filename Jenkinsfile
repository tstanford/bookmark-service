pipeline {
    agent any 
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
