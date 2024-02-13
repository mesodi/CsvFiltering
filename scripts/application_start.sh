#!/bin/bash

# Copy Dockerfile, pom.xml, and source code
#sudo cp -f /tmp/codedeploy-deployment-staging-area/docker-compose.yml /home/ec2-user/


sudo cp -f /tmp/codedeploy-deployment-staging-area/dockerfile /home/ec2-user/
sudo cp -f /tmp/codedeploy-deployment-staging-area/pom.xml /home/ec2-user/
sudo cp -rf /tmp/codedeploy-deployment-staging-area/src /home/ec2-user/


# Build and run the Spring Boot application
#
cd /home/ec2-user/
# Stop and remove the existing "fullstackapp" Docker container if it's running
    echo "Stopping and removing the existing 'springbootcamel' container..."
    docker stop CsvFiltering
    docker rm CsvFiltering
mvn package .
docker build -t johnnie3000/csvfiltering:tagname .


docker run -d --name csvfiltering -p 80:8080 johnnie3000/csvfiltering:tagname


