#!/bin/bash

# Run the mvn command to check against all of our postive and negative test cases
mvn -Dtest=*Cases surefire-report:report

# Results can be found in target/site/surefire-report
