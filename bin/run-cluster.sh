#!/bin/sh

COUNT=1
if [ $# -eq 1 ]; then
	COUNT=$1
elif [ $# -gt 1 ]; then
    echo "Usage: $0 [<server count parameter>]"
    exit 1
fi

export CLUSTER=database
export GROUP=sg-c3756fb7 # quick-start-1, same as puppet master

if [ -z "EC2_PRIVATE_KEY" ]; then echo "ERROR: EC2_PRIVATE_KEY not set, make sure to include .bashrc to cinfigure EC2 access"; exit 1; fi
if [ "`id -un`" != "root" ]; then echo 'ERROR: You must run this as root'; exit 1; fi

## See http://aws.amazon.com/ec2/instance-types/
#export INSTANCE_TYPE=t1.micro	# 613 MB, small CU with up to 2 CU in bursts
export INSTANCE_TYPE=m1.small	# 1.7 GB, 1 CU
#export INSTANCE_TYPE=m1.medium	# 3.75 GB, 2 CU

export PUPPET_MASTER=ec2-176-34-218-57.eu-west-1.compute.amazonaws.com

read -r -d '' USER_DATA <<EOF
#cloud-config

packages:
 - puppet

runcmd:
 - wget -O /etc/puppet/puppet.conf https://raw.github.com/iterate/codecamp2012/puppet/install-puppet.sh
 - sh install-puppet.sh
 - FACTER_clustername=$CLUSTER puppetd -v --server $PUPPET_MASTER -w 60
EOF

echo "Going to run cluster of $COUNT instance(s)..."

# See http://docs.amazonwebservices.com/AWSEC2/latest/CommandLineReference/ApiReference-cmd-RunInstances.html
AMI=ami-3850624c
OUT=$(ec2-run-instances $AMI -n $COUNT -g $GROUP -k StarClusterCodeCamp12 -d "$USER_DATA" --instance-type $INSTANCE_TYPE)

# Get id of the first instance
# OBS: Doesn't work if there is no instance at all in the output
#INSTANCE=$(echo "$OUT" | awk '/INSTANCE/ { print $2; exit 0 }')

echo "Waiting for the instances to come up..."
while true; do
       DESCR=$(ec2-describe-instances)
       echo "$DESCR" | awk "/INSTANCE.*pending.*StarClusterCodeCamp12/ {exit 1}" && break
       echo "Some instances pending, waiting..."
       sleep 4
done
echo "Cluster instance hostnames:"
echo "$DESCR" | awk '/INSTANCE.*StarClusterCodeCamp12/ {print $4 " for id " $2}'
