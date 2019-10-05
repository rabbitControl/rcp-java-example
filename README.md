# rcp-java-example
examples for rcp-java

to run the test-server use script `startserver.sh` to or use one of these:

show help
```
./gradlew run -Dexec.args="-h"
```

show available startup-methods
```
./gradlew run -Dexec.args="-m"
```

run server with startup-method "exposeSingleFloat" 
```
./gradlew run -Dexec.args="-c exposeSingleFloat"
```
