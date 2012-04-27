#!/bin/sh
if [ $# -ne 2 ]
then
    echo "Usage: $0 <cluster name: database|testclient> <server count parameter>"
    exit -1
fi
if [ "$1" = "database" ]; then
	export CLUSTER=database
	export GROUP=sg-15001f79 #codecamp-databases
elif [ "$1" = "testclient" ]; then
	export CLUSTER=testclient
	export GROUP=sg-13001f7f #codecamp-testclients
else
	echo "Invalid cluster name '$1'; expected: database or testclient"
	exit -1
fi
COUNT=$2

if [ -z "EC2_PRIVATE_KEY" ]; then echo "ERROR: EC2_PRIVATE_KEY not set, make sure to include .bashrc to cinfigure EC2 access"; exit -1; fi

# See http://aws.amazon.com/ec2/instance-types/
#export INSTANCE_TYPE=t1.micro	# 613 MB, small CU with up to 2 CU in bursts
export INSTANCE_TYPE=m1.small	# 1.7 GB, 1 CU
#export INSTANCE_TYPE=m1.medium	# 3.75 GB, 2 CU

export PUPPET_MASTER=ec2-176-34-218-57.eu-west-1.compute.amazonaws.com
#export PUPPET_MASTER=ip-10-56-71-201.eu-west-1.compute.internal

read -r -d '' USER_DATA <<EOF
#cloud-config

packages:
- puppet

runcmd:
 - mkdir -p /etc/puppet
 - wget -O /etc/puppet/puppet.conf https://raw.github.com/iterate/codecamp2012/puppet/puppet.conf
 - FACTER_clustername=$CLUSTER puppetd -v --server $PUPPET_MASTER -w 60
EOF

# See http://docs.amazonwebservices.com/AWSEC2/latest/CommandLineReference/ApiReference-cmd-RunInstances.html
AMI=ami-3850624c
OUT=$(ec2-run-instances $AMI -n $COUNT -g  $GROUP -k StarClusterCodeCamp12 -d "$USER_DATA" --instance-type $INSTANCE_TYPE --subnet subnet-bd1732d4)

# AWK: RS is EOL regexp; RT captures the last RS found and is thus either INSTANCE or '' for the last record not followed by this
# OBS: Doesn't work if there is no instance at all in the output
INSTANCE=$(echo $OUT | awk 'BEGIN {RS="INSTANCE"} { if (RT != "INSTANCE") {print $1 }}')

echo "Waiting for the 1st instance to come up..."
while ! ec2-describe-instances | awk "/$INSTANCE.*?pending/ {print \"Instance $INSTANCE not running yet...; LINE: \" \$0; exit 1}"; do sleep 4; done

echo "Going to assign IP 46.51.196.99 to the instance '$INSTANCE': ec2-associate-address -a eipalloc-23e1c54a -i $INSTANCE"
ec2-associate-address -a eipalloc-23e1c54a -i $INSTANCE

