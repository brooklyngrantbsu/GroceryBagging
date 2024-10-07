#!/bin/bash

# Check if theres an arg
if [ $# -eq 0 ]; then
    echo "Usage: ./bagit <filename>"
    exit 1
fi

# run
javac BagIt.java
java BagIt $1
