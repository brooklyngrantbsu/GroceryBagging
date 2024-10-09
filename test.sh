#!/bin/bash

# make file to clean up java files
make clean
make

# loop through bag files 0 to 15
for i in {0..15}
do
    echo "Bag $i:"
    output=$(./bagit.sh "bag$i.txt")
    echo "$output"
    
    # Check if the output contains the word "failure"
    if echo "$output" | grep -q "failure"; 
    then
        echo "Bagging failed for bag$i.txt. No need to check bag for solution."
    else
        # Run CheckBag.jar if successful
        java -jar CheckBag.jar "bag$i.txt" "bag$i.txt.out"
    fi
    
    echo "----------------------------------------"
done
