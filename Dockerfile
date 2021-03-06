# Small linux distro with the Java Development Kit for compiling/running Java
FROM openjdk:alpine

# Do things in container at path /app
WORKDIR /app

# Copy files that need to live in the container into /app in the container
ADD ./app/container /app

# Compile tester code beforehand
RUN javac -classpath '.:lib/minimal-json/minimal-json-0.9.5-sources.jar' *.java

# Port 80 should be accessible outside of the container
EXPOSE 80

CMD javac $PROBLEM_NAME.java && \
    java -classpath '.:lib/minimal-json/minimal-json-0.9.5.jar' Tester
