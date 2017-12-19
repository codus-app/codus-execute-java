#!/usr/local/bin/zsh

# Make the container
id=$(docker create --rm codus-execute-java)

# Copy the user's code into the container
docker cp $(dirname $0)/tests.json $id:/tests.json
# Copy the JSON representation of test cases into the container
docker cp $(dirname $0)/Solution.java $id:/Solution.java

# Run
docker start -a $id
