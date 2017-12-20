echo 'Compiling...'
javac -classpath '.:lib/gson/gson-2.8.2-sources.jar' *.java # Include GSON in classpath
echo 'Running...'
java -classpath '.:lib/gson/gson-2.8.2.jar' Tester  # Include GSON in classpath
