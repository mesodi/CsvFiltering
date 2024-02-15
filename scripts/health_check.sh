#!/bin/bash

# Check if the instance is healthy
if [ "$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)" = "200" ]; then
    echo "Successfully pulled root page."
else
    echo "Server did not come up after expected time. Failing."
fi

# Check if the application is running
if docker ps -f "name=springbootcamel" | grep -q "springbootcamel"; then
    echo "Application is running."
else
    echo "Application is not running."
fi


