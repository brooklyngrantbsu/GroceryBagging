#!/bin/bash

# make file to clean up java files
make clean
make

# loop through bag files 0 to 15
for i in {0..15}
do
    filename="bag$i.txt"
    
    echo "Testing File $filename:"
    outputBagit=$(./bagit.sh "$filename")
    echo "$outputBagit"
    
    # Check if the output contains the word "failure"
    if echo "$outputBagit" | grep -q "failure"; 
    then
        echo "Bagging failed for $filename. See if this is supposed to fail with cspsolve."
        outputCSPSolve=$(./cspsolve "$filename")
        echo "cspsolve output: $outputCSPSolve"

        if echo "$outputCSPSolve" | grep -q "failure";
        then
            echo "Test File $i PASS";
        else
            echo "Test File $i FAIL";
        fi

    else
        # Run CheckBag.jar if successful
        outputCheckBag=$(java -jar CheckBag.jar "$filename" "$filename.out")
        echo "checkbag output: $outputCheckBag"

        if echo "$outputCheckBag" | grep -q "Good solution!";
        then
            echo "Test File $i PASS";
        else
            echo "Test File $i FAIL";
        fi
    fi
    
    echo "----------------------------------------"
done