# 🚀 AWS Auto Scaling + ALB + Spring Boot + Docker + Jenkins CI/CD

A production-style cloud deployment project demonstrating an end-to-end CI/CD workflow using **GitHub, Jenkins, Maven, Docker, Docker Hub, AWS EC2, Auto Scaling Group, Application Load Balancer, Target Group, Launch Template, VPC, Subnet and Internet Gateway**.

The Spring Boot application is packaged as a Docker image and automatically deployed to EC2 instances managed by an AWS Auto Scaling Group.

The Application Load Balancer distributes incoming traffic across healthy EC2 instances, while Jenkins automates the build, containerization and AWS deployment process.

---

## 📌 Project Overview

This project demonstrates a complete application deployment lifecycle:

```text
Developer
    |
    v
  GitHub
    |
    v
 Jenkins CI/CD
    |
    v
 Maven Build
    |
    v
 Docker Build
    |
    v
 Docker Hub
    |
    v
 AWS Auto Scaling Instance Refresh
    |
    v
 Auto Scaling Group
    |
    v
 EC2 Instances
    |
    v
 Docker Containers
    |
    v
 Spring Boot Application
    |
    v
 Target Group
    |
    v
 Application Load Balancer
    |
    v
    User

The project combines Java application development, containerization, CI/CD automation and AWS cloud infrastructure into one deployment workflow.

🏗️ System Architecture
Application Architecture
                         INTERNET
                            |
                            v
                +-----------------------+
                | Application Load      |
                | Balancer (ALB)        |
                | HTTP :80              |
                +-----------+-----------+
                            |
                            v
                +-----------------------+
                | Target Group          |
                | ASG-Target-Group      |
                | Port 80               |
                | Health Check: /       |
                +-----------+-----------+
                            |
             +--------------+--------------+
             |              |              |
             v              v              v
          +------+       +------+       +------+
          | EC2  |       | EC2  |       | EC2  |
          | #1   |       | #2   |       | #3   |
          +--+---+       +--+---+       +--+---+
             |              |              |
             v              v              v
        +---------+    +---------+    +---------+
        | Docker  |    | Docker  |    | Docker  |
        |Container|    |Container|    |Container|
        +----+----+    +----+----+    +----+----+
             |              |              |
             +--------------+--------------+
                            |
                            v
                  Spring Boot Application
                          Port 8080
🔄 CI/CD Pipeline

The deployment pipeline is automated using Jenkins.

       Developer
           |
           v
        GitHub
           |
           v
    +-------------+
    |   Jenkins   |
    |   CI / CD   |
    +------+------+
           |
           v
     Maven Build
           |
           v
     Docker Build
           |
           v
      Docker Hub
           |
           v
 AWS Auto Scaling
 Instance Refresh
           |
           v
    EC2 Instances
           |
           v
    Docker Container
           |
           v
 Spring Boot App
           |
           v
      AWS ALB
Jenkins Pipeline Stages

The Jenkins pipeline contains the following stages:

1. Build
2. Docker Build
3. Docker Push
4. Deploy to AWS ASG
Build

Jenkins builds the Spring Boot application using Maven.

mvn clean package
Docker Build

Jenkins creates a Docker image containing the Spring Boot application.

aws-asg-springboot
Docker Push

The Docker image is pushed to Docker Hub.

siddh342/aws-asg-springboot:latest
Deploy to AWS ASG

Jenkins starts an AWS Auto Scaling Instance Refresh.

The existing EC2 instances are gradually replaced using the configured Launch Template.

Jenkins monitors the Instance Refresh until it becomes:

Successful

This provides an automated deployment mechanism without manually updating each EC2 instance.

🐳 Docker

The application is containerized using Docker.

Docker Image
siddh342/aws-asg-springboot:latest

Docker Hub:

https://hub.docker.com/r/siddh342/aws-asg-springboot

The container runs the Spring Boot application on:

Container Port: 8080

The EC2 instances expose:

Port 80 → Container Port 8080

The deployment therefore follows:

ALB :80
   |
   v
EC2 :80
   |
   v
Docker Container :8080
   |
   v
Spring Boot
☕ Spring Boot Application

The backend application is developed using:

Java 17
Spring Boot
Spring Web
REST API
Maven
Application Endpoints
Dashboard
GET /

Displays the professional cloud deployment dashboard.

Health Check
GET /health

Response:

Application is Healthy

The /health endpoint is used to verify that the Spring Boot application is running correctly.

☁️ AWS Infrastructure
Amazon VPC

The application infrastructure is deployed inside an AWS VPC.

VPC
ASG-ALB-VPC
CIDR: 10.0.0.0/16

The VPC provides the private networking environment for the application infrastructure.

Subnet

The EC2 instances and load-balancing infrastructure operate within the configured AWS subnets.

The subnet layer provides network isolation and routing inside the VPC.

Internet Gateway

The Internet Gateway provides internet connectivity for the public-facing components.

Internet
    |
    v
Internet Gateway
    |
    v
VPC
⚖️ Application Load Balancer

The Application Load Balancer provides a single entry point for users.

Configuration
Load Balancer:
ASG-Application-LB

Type:
Application Load Balancer

Scheme:
Internet-facing

Listener:
HTTP :80

The ALB forwards requests to the configured Target Group.

🎯 Target Group

Target Group:

ASG-Target-Group

Target Port:

80

Health Check:

Protocol: HTTP
Port: 80
Path: /

The Target Group checks whether each EC2 instance is healthy before sending traffic to it.

Current architecture:

ALB
 |
 v
Target Group
 |
 +---- EC2 #1
 |
 +---- EC2 #2
 |
 +---- EC2 #3

All healthy instances can receive application traffic.

📈 Auto Scaling Group

Auto Scaling Group:

ASG-Web-Servers
Capacity Configuration
Setting	Value
Minimum Capacity	2
Desired Capacity	3
Maximum Capacity	3
Health Checks	EC2 + ELB
Health Check Grace Period	300 seconds

The Auto Scaling Group maintains the required number of EC2 instances.

When an instance becomes unhealthy or is terminated, the Auto Scaling Group can launch a replacement instance.

⚙️ Launch Template

Launch Template:

ASG-Launch-Template

The Launch Template defines how new EC2 instances are created.

The configuration includes:

Operating System:
Amazon Linux 2023

Instance Type:
t3.micro

Storage:
8 GiB

Docker:
Installed through User Data

Application:
Dockerized Spring Boot
Launch Template User Data

The EC2 instances automatically install Docker and start the application container during initialization.

#!/bin/bash

dnf update -y

dnf install -y docker

systemctl enable docker
systemctl start docker

docker pull siddh342/aws-asg-springboot:latest

docker run -d \
  --name aws-asg-springboot-container \
  -p 80:8080 \
  siddh342/aws-asg-springboot:latest

This allows newly launched EC2 instances to automatically obtain and start the current Docker image.

🔄 Auto Scaling Instance Refresh

Jenkins uses AWS Auto Scaling Instance Refresh during deployment.

Deployment flow:

New Docker Image
       |
       v
Docker Hub
       |
       v
Jenkins
       |
       v
Start Instance Refresh
       |
       v
Launch New EC2 Instance
       |
       v
Launch Template User Data
       |
       v
Install Docker
       |
       v
Pull Docker Image
       |
       v
Run Spring Boot Container
       |
       v
Target Group Health Check
       |
       v
Healthy
       |
       v
Continue Instance Replacement

This allows Jenkins to automate replacement of the EC2 instances using the new application image.

🔐 IAM

Jenkins uses an AWS IAM role to interact with AWS services.

IAM permissions are used for:

Auto Scaling
EC2
Launch Template
Load Balancer read operations

The Jenkins AWS role is:

Jenkins-ASG-Deployment-Role

Credentials such as Docker Hub credentials are stored using Jenkins Credentials rather than being hard-coded inside the pipeline.

🔒 Security

The project uses AWS Security Groups to control network traffic.

Important security considerations include:

Internet → ALB :80
ALB → EC2 :80
EC2 → Docker Container :8080

The Jenkins EC2 uses its IAM role for AWS access.

The Jenkins private SSH key is excluded from Git using .gitignore.

Sensitive Docker credentials are stored in Jenkins Credentials.

📊 Current Deployment Architecture
                     INTERNET
                         |
                         v
              +---------------------+
              | Internet Gateway    |
              +----------+----------+
                         |
                         v
       +--------------------------------------+
       |              VPC                     |
       |         ASG-ALB-VPC                  |
       |                                      |
       |  +-------------------------------+   |
       |  | Application Load Balancer     |   |
       |  | ASG-Application-LB            |   |
       |  +---------------+---------------+   |
       |                  |                   |
       |                  v                   |
       |       +-----------------------+      |
       |       | Target Group          |      |
       |       | ASG-Target-Group      |      |
       |       +-----------+-----------+      |
       |                   |                  |
       |        +----------+----------+       |
       |        |          |          |       |
       |        v          v          v       |
       |      EC2 #1     EC2 #2     EC2 #3   |
       |        |          |          |       |
       |        v          v          v       |
       |      Docker     Docker     Docker    |
       |        |          |          |       |
       |        +----------+----------+       |
       |                   |                  |
       |                   v                  |
       |          Spring Boot App             |
       |                                      |
       +--------------------------------------+
🧰 Technologies Used
Application
Java 17
Spring Boot
Spring Web
REST API
Maven
HTML
CSS
JavaScript
CI/CD & DevOps
Git
GitHub
Jenkins
Jenkins Pipeline
Docker
Docker Hub
CI/CD
AWS Cloud
Amazon EC2
Amazon VPC
Subnet
Internet Gateway
Route Table
Security Groups
Application Load Balancer
Target Group
Auto Scaling Group
Launch Template
IAM
Auto Scaling Instance Refresh
Operating System
Amazon Linux 2023
Windows
📂 Project Structure
aws-asg-app/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── aws_asg_app/
│       │       ├── AwsAsgAppApplication.java
│       │       └── AwsAsgController.java
│       │
│       └── resources/
│           └── static/
│               └── index.html
│
├── Dockerfile
├── Jenkinsfile
├── pom.xml
├── README.md
└── .gitignore
⚙️ Jenkinsfile

The Jenkins pipeline automates the complete deployment process.

Main stages:

Build
Docker Build
Docker Push
Deploy to AWS ASG

The deployment stage starts an AWS Instance Refresh and waits until the deployment is completed successfully.

The pipeline also prevents concurrent deployments and uses a timeout to prevent indefinitely running builds.

🚀 Deployment Workflow

The complete deployment process is:

1. Developer modifies application
             |
             v
2. Push code to GitHub
             |
             v
3. Jenkins starts CI/CD pipeline
             |
             v
4. Maven builds Spring Boot application
             |
             v
5. Docker image is created
             |
             v
6. Docker image pushed to Docker Hub
             |
             v
7. Jenkins starts AWS Instance Refresh
             |
             v
8. ASG launches/replaces EC2 instances
             |
             v
9. Launch Template User Data installs Docker
             |
             v
10. Docker pulls latest application image
             |
             v
11. Spring Boot container starts
             |
             v
12. Target Group performs health check
             |
             v
13. Healthy instances receive ALB traffic
             |
             v
14. New version becomes live
🧪 Deployment Verification

The deployment can be verified at multiple levels.

Jenkins
Build → SUCCESS
Docker Hub
Image → Available
Auto Scaling Group
Instances → Healthy
Instances → InService
Target Group
Healthy Targets → 3/3
Application Load Balancer
State → Active
Application
/       → Dashboard
/health → Application is Healthy
🎯 Key Project Objectives

This project demonstrates practical knowledge of:

Spring Boot application development
REST API development
Maven application builds
Docker containerization
Docker Hub image management
Jenkins CI/CD
GitHub integration
AWS EC2 deployment
Auto Scaling Groups
Application Load Balancer
Target Group health checks
Launch Templates
VPC networking
Subnets
Internet Gateway
IAM roles
Automated Instance Refresh
High availability
Rolling application deployment
💡 Key DevOps Concepts Demonstrated
Continuous Integration

Jenkins automatically builds the application and creates a Docker image whenever new source code is introduced into the pipeline.

Continuous Delivery / Deployment

The Docker image is pushed to Docker Hub and Jenkins automatically triggers deployment to the AWS Auto Scaling environment.

Containerization

The Spring Boot application runs inside Docker containers rather than directly on the EC2 host.

High Availability

Multiple EC2 instances are maintained by the Auto Scaling Group and traffic is distributed through the Application Load Balancer.

Self-Healing Infrastructure

If an EC2 instance is terminated or becomes unhealthy, the Auto Scaling Group can create a replacement instance.

Automated Infrastructure Deployment

The Launch Template User Data automatically configures Docker and starts the latest application image on newly launched instances.

🌐 Live Application
Application
http://ASG-Application-LB-1673207831.ap-south-1.elb.amazonaws.com/
Health Check
http://ASG-Application-LB-1673207831.ap-south-1.elb.amazonaws.com/health
📸 Screenshots

Recommended screenshots for the repository:

01 - Application Dashboard
02 - Jenkins Pipeline
03 - Jenkins Successful Build
04 - Docker Hub Image
05 - EC2 Auto Scaling Group
06 - ALB
07 - Target Group Healthy Targets
08 - Launch Template
09 - AWS Instance Refresh
10 - Final Live Application
🔮 Future Enhancements

Possible future improvements include:

GitHub Webhook automation
HTTPS using AWS Certificate Manager
CloudWatch monitoring
CloudWatch alarms
Auto Scaling policies
AWS ECR instead of Docker Hub
Terraform Infrastructure as Code
Jenkins notifications
Environment-specific deployments
Immutable Docker image tags/digests
Production-grade secrets management
👨‍💻 Author
Sidhanta Sahoo

GitHub:

https://github.com/sidhantasahoo175

⭐ Project Summary

This project demonstrates an end-to-end Java + DevOps + AWS cloud deployment architecture where a Spring Boot application is containerized with Docker, built and deployed through Jenkins CI/CD, stored on Docker Hub, and automatically rolled out across EC2 instances managed by an AWS Auto Scaling Group and fronted by an Application Load Balancer.

The project combines application development, containerization, CI/CD automation, cloud infrastructure, networking, load balancing, health checks and high-availability deployment into a single practical implementation.