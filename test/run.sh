#!/usr/local/bin/zsh

# Make the container
id=$(docker create --rm codus-execute-java)

# Copy the user's code into the container
docker cp Solution.java $id:Solution.java
# Copy the JSON representation of test cases into the container
docker cp tests.json $id:tests.json

# Run
docker start -a $id
