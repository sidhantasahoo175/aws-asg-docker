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
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t aws-asg-springboot .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        docker tag aws-asg-springboot:latest \
                            "$DOCKER_USERNAME/aws-asg-springboot:latest"

                        docker push \
                            "$DOCKER_USERNAME/aws-asg-springboot:latest"
                    '''
                }
            }
        }

        stage('Deploy to AWS ASG') {
            steps {
                sh '''
                    echo "Starting AWS Auto Scaling Instance Refresh..."

                    REFRESH_ID=$(aws autoscaling start-instance-refresh \
                        --auto-scaling-group-name ASG-Web-Servers \
                        --preferences '{"MinHealthyPercentage":66,"InstanceWarmup":120,"SkipMatching":false}' \
                        --region ap-south-1 \
                        --query 'InstanceRefreshId' \
                        --output text)

                    echo "Instance Refresh ID: $REFRESH_ID"

                    while true
                    do
                        STATUS=$(aws autoscaling describe-instance-refreshes \
                            --auto-scaling-group-name ASG-Web-Servers \
                            --instance-refresh-ids "$REFRESH_ID" \
                            --region ap-south-1 \
                            --query 'InstanceRefreshes[0].Status' \
                            --output text)

                        echo "Instance Refresh Status: $STATUS"

                        if [ "$STATUS" = "Successful" ]; then
                            echo "Deployment completed successfully."
                            break
                        fi

                        if [ "$STATUS" = "Failed" ] || [ "$STATUS" = "Cancelled" ]; then
                            echo "Deployment failed with status: $STATUS"
                            exit 1
                        fi

                        sleep 20
                    done
                '''
            }
        }
    }
}