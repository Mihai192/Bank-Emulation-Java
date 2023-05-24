#!/bin/bash

cd ..
# javac -d ../build -classpath ../lib/mysql-connector-j-8.0.33.jar: ../src/App.java

javac -d build -classpath lib/mysql-connector-j-8.0.33.jar: src/App.java
cd scripts