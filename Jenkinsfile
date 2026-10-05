pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t aws-asg-springboot .'
            }
        }

        stage('Docker Login') {
            steps {
                bat 'docker login -u siddh342'
            }
        }

        stage('Docker Push') {
            steps {
                bat 'docker tag aws-asg-springboot:latest siddh342/aws-asg-springboot:latest'
                bat 'docker push siddh342/aws-asg-springboot:latest'
            }
        }
    }
}