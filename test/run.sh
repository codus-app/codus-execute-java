#!/usr/local/bin/zsh

# Make the container
id=$(docker create codus-execute-java)

# Copy the user's code into the container
docker cp $(dirname $0)/1/tests.json $id:/app/tests.json
# Copy the JSON representation of test cases into the container
docker cp $(dirname $0)/1/Solution.java $id:/app/Solution.java

# Run
docker start -a $id

# Copy log file
docker cp $id:/app/results.json results.json

# Remove the container
docker rm $id
