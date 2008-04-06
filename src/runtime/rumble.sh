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

java rumble.Runtime.Main $1 $2
