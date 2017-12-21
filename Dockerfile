# Small linux distro with the Java Development Kit for compiling/running Java
FROM openjdk:alpine

# Do things in container at path /app
WORKDIR /app

# Copy files from this directory into /app in the container
ADD . /app

# Port 80 should be accessible outside of the container
EXPOSE 80

CMD {                                                                                \
      echo 'Compiling...'                                                         && \
      javac -classpath '.:lib/minimal-json/minimal-json-0.9.5-sources.jar' *.java && \
      echo 'Running...'                                                           && \
      java -classpath '.:lib/minimal-json/minimal-json-0.9.5.jar' Tester;            \
    } > log.txt
