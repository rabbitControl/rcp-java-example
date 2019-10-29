#!/bin/bash

if [ $# -eq 0 ]
then
  ./gradlew run -Dexec.args="-c exposeSingleFloat"
else
  ./gradlew run -Dexec.args="$*"
fi
