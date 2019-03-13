#!/bin/bash

# Run the mvn command to check against all of our test cases 
mvn -Dtest=*PositiveCases surefire-report:report

# Results can be found in target/site/surefire-report
