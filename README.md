\# AWS Auto Scaling + Application Load Balancer + Docker



\## 📌 Project Overview



This project demonstrates how to deploy a containerized web application on AWS using an Application Load Balancer, Auto Scaling Group, EC2, Docker, and NGINX.



The application is packaged into a Docker image and hosted on Docker Hub. AWS EC2 instances automatically pull the Docker image and run the container through Launch Template User Data.



The Auto Scaling Group maintains the required number of EC2 instances, while the Application Load Balancer distributes incoming traffic across healthy instances.



\---



\## 🏗️ Architecture



```text

&#x20;                        Internet

&#x20;                           |

&#x20;                           v

&#x20;             Application Load Balancer

&#x20;                        HTTP :80

&#x20;                           |

&#x20;                           v

&#x20;                   Target Group

&#x20;                 Health Check: /

&#x20;                           |

&#x20;             +-------------+-------------+

&#x20;             |             |             |

&#x20;             v             v             v

&#x20;          EC2 #1        EC2 #2        EC2 #3

&#x20;             |             |             |

&#x20;             v             v             v

&#x20;         Docker         Docker         Docker

&#x20;         Container      Container      Container

&#x20;             |             |             |

&#x20;             +-------------+-------------+

&#x20;                           |

&#x20;                        NGINX

&#x20;                           |

&#x20;                     Web Application



&#x20;                   Auto Scaling Group

&#x20;                           |

&#x20;                           v

&#x20;                   Launch Template

&#x20;                           |

&#x20;                           v

&#x20;                   Docker Hub Image

☁️ AWS Services Used

Amazon VPC

Amazon EC2

Application Load Balancer (ALB)

Target Group

Auto Scaling Group (ASG)

Launch Template

Internet Gateway

Route Table

Security Groups

🐳 Docker



The web application is containerized using Docker.



Docker Image

siddh342/aws-asg-docker:latest

Dockerfile

FROM nginx:latest



COPY index.html /usr/share/nginx/html/index.html



EXPOSE 80



The Docker image uses the official NGINX image and copies the custom HTML application into the NGINX web directory.



🚀 Deployment Flow

Docker Application

&#x20;       |

&#x20;       v

Docker Build

&#x20;       |

&#x20;       v

Docker Hub

&#x20;       |

&#x20;       v

AWS Launch Template

&#x20;       |

&#x20;       v

Auto Scaling Group

&#x20;       |

&#x20;       v

EC2 Instance

&#x20;       |

&#x20;       v

Docker Container

&#x20;       |

&#x20;       v

NGINX

&#x20;       |

&#x20;       v

Target Group

&#x20;       |

&#x20;       v

Application Load Balancer

⚙️ Launch Template



The Launch Template uses:



Amazon Linux 2023

t3.micro

8 GiB gp3 storage

Security Group allowing HTTP traffic from the ALB

Docker installation through User Data

User Data

\#!/bin/bash



dnf update -y



dnf install -y docker



systemctl enable docker

systemctl start docker



docker pull siddh342/aws-asg-docker:latest



docker run -d \\

&#x20; --name aws-asg-container \\

&#x20; -p 80:80 \\

&#x20; siddh342/aws-asg-docker:latest

📈 Auto Scaling Configuration



The Auto Scaling Group is configured with:



Setting	Value

Minimum capacity	2

Desired capacity	3

Maximum capacity	3

Health checks	EC2 + ELB

Health check grace period	300 seconds



The Auto Scaling Group automatically launches a replacement EC2 instance when an instance is terminated.



⚖️ Application Load Balancer



The Application Load Balancer:



Is internet-facing

Listens on HTTP port 80

Uses the configured Target Group

Distributes traffic across healthy EC2 instances

Performs health checks through the Target Group

Health Check

Protocol: HTTP

Port: 80

Path: /

🔄 Auto Scaling Demonstration



As part of the project, EC2 instances were intentionally terminated.



The Auto Scaling Group detected that the desired capacity was no longer available and automatically launched replacement instances.



The new instances:



Booted Amazon Linux 2023

Installed Docker

Pulled the Docker image from Docker Hub

Started the Docker container

Registered with the Target Group

Passed the ALB health check



The application remained available through the Application Load Balancer.



🖥️ Application



The application displays:



AWS Auto Scaling + Load Balancer + Docker



NGINX Web Server



This web application is running inside a Docker container.



Container: Docker



Application Load Balancer is distributing traffic.

🛠️ Technologies Used

Cloud

AWS

EC2

VPC

Application Load Balancer

Auto Scaling

Target Groups

Security Groups

Containerization

Docker

Docker Hub

NGINX

Operating System

Amazon Linux 2023

Version Control

Git

GitHub

Web

HTML

CSS

NGINX

📂 Project Structure

aws-asg-docker/

│

├── Dockerfile

├── index.html

└── README.md

🎯 Project Objectives

Understand AWS networking fundamentals

Deploy applications on EC2

Configure Application Load Balancer

Configure Target Group health checks

Implement Auto Scaling

Containerize a web application using Docker

Host Docker images on Docker Hub

Automatically deploy Docker containers using EC2 User Data

Demonstrate automatic EC2 instance replacement

Manage the project using Git and GitHub

🔮 Future Enhancements



The project will be extended with:



Spring Boot application

MySQL database

Jenkins CI/CD pipeline

Automated Docker image builds

Amazon ECR

Automated deployment

HTTPS using AWS Certificate Manager

CloudWatch monitoring

Auto Scaling policies

Terraform Infrastructure as Code

👨‍💻 Author



Sidhanta Sahoo



GitHub: https://github.com/sidhantasahoo175

