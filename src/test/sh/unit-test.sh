APP_JAR_FILE=taskman-api-1.0-SNAPSHOT.jar
JUNIT_VER=5.7.2
API_GUARDIAN_VER=1.1.2
JUNIT_CONSOLE_VER=1.8.0
WORK_DIR=$(pwd)/target

cd "$WORK_DIR"
wget "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/${JUNIT_VER}/junit-jupiter-api-${JUNIT_VER}.jar" -P lib/
wget "https://repo1.maven.org/maven2/org/apiguardian/apiguardian-api/${API_GUARDIAN_VER}/apiguardian-api-${API_GUARDIAN_VER}.jar" -P lib/
wget "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_CONSOLE_VER}/junit-platform-console-standalone-${JUNIT_CONSOLE_VER}.jar" -P lib/
wget "https://repo1.maven.org/maven2/io/dropwizard/dropwizard-testing/2.0.25/dropwizard-testing-2.0.25.jar" -P lib/

#mkdir -p test-classes
javac -sourcepath ../src/test/java/ -cp ${APP_JAR_FILE}:lib/junit-jupiter-api-${JUNIT_VER}.jar:lib/apiguardian-api-${API_GUARDIAN_VER}.jar:lib/dropwizard-testing-2.0.25.jar -d test-classes ../src/test/java/org/muntasir/lab/*.java

# * Run Test Suit to validate
java -jar "lib/junit-platform-console-standalone-${JUNIT_CONSOLE_VER}.jar" -cp ${APP_JAR_FILE}:lib/dropwizard-testing-2.0.25.jar:test-classes/ --scan-classpath

cd ..