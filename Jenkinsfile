pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                script {
                    ./gradlew build -x test
                }
            }
        }
        stage('Test') { 
            steps {
                ./gradlew build test
            }
        }
        stage('Deploy') { 
            steps {
                echo "Deploying" 
            }
        }
    }
}
