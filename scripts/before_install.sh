#!/bin/bash
# installing docker, maven, java, x-ray, codedeploy-agent >> if not installed


cd /home/ec2-user/
# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "Docker is not installed. Installing..."
    sudo yum install -y docker
    sudo service docker start
    sudo systemctl enable docker

    sudo usermod -aG docker ec2-user

    sudo chmod 666 /var/run/docker.sock

    wget https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -O /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
else
    echo "Docker is already installed."
fi


# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Installing..."
    sudo yum install -y java
else
    echo "Java is already installed."
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Installing..."
    sudo yum install -y maven
else
    echo "Maven is already installed."
fi


sudo yum clean all