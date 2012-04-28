#!/bin/sh
IDS=$(ec2-describe-instances | awk '/INSTANCE.*running.*StarClusterCodeCamp12/ {print $2}')
echo "IDs of running cluster instances to be terminated: " $IDS
echo $IDS | xargs ec2-terminate-instances
