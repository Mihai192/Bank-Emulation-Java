#!/bin/bash

# java -classpath ../lib/mysql-connector-j-8.0.33.jar:../build src.App
cd ..
java -classpath lib/mysql-connector-j-8.0.33.jar:./build src.App
cd scripts