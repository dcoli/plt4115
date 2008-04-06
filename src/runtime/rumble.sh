#!/bin/sh

# PLT: Team Maximum Caps Lock
# Basic shell script to run Rumble.
# Check to see if the correct number of arguments were passed before running the jar file

ARG_ERROR=65
MINIMUM_ARGS=1

if [[ $# < $MINIMUM_ARGS ]]; then
	echo "Usage: `basename $0` simulation.rus"
	exit $ARG_ERROR
fi

java rumble.compiler.Compiler.Main $1
# the following must be done by Compiler.Main:
# mkdir build, build/rumble, build/rumble/runtime
# create build/rumble/runtime/Runtime.java
# compile/create build/rumble/Environment.java
# compile/create build/rumble/Participant.java

# if Compiler.Main does not return an error...
javac build/rumble/*.java
java build/rumble.runtime.Runtime.Main $2
rm -rf build
