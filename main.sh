echo 'Compiling...'
javac -classpath '.:lib/minimal-json/minimal-json-0.9.5-sources.jar' *.java # Include GSON in classpath
echo 'Running...'
java -classpath '.:lib/minimal-json/minimal-json-0.9.5.jar' Tester  # Include GSON in classpath
