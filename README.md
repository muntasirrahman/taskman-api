
## Build and Run

```shell
mvn clean package
```
Then go to target directory, and execute the jar file.

```shell
cd target
java -jar task-server-1.0-SNAPSHOT.jar
```


## Tests

```shell
mvn clean test
```

To generate Test Report
```shell
mvn clean test site
```
The generated HTML files is at 
```shell
$PROJECT_ROOT/target/site
```
