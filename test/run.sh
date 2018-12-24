#!/usr/local/bin/zsh

# Make the container
id=$(docker create --env PROBLEM_NAME='DoubleTest' codus-execute-java)

# Copy the user's code into the container
docker cp $(dirname $0)/pass/4/tests.json $id:/app/tests.json
# Copy the JSON representation of test cases into the container
docker cp $(dirname $0)/pass/4/DoubleTest.java $id:/app/DoubleTest.java

# Run
docker start -a $id

# Copy log file
docker cp $id:/app/out.json out.json

# Remove the container
docker rm $id > /dev/null
