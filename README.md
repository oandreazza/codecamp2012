CodeCamp Puppet Repository
==================

Puppet repository used in the Code Camp 2012 Amazon EC2 infrastructure to set up new servers with Java, our database service Graft, users etc. It might be interesting for anybody interested in setting up infrastructure in EC2 with Puppet.

Why Amazon Linux AMI?
-------------------

We used virtual machines based on the  Amazon Linux AMI because it comes with the Amazon EC2 command-line tools preinstalled and has Puppet available in its repository (though not the latest version).

About the Setup
-------------

We had a Micro instance with Amazon Linux in [Amazon EC2](http://aws.amazon.com/ec2/), running a Puppet Master. The files in this repository were checked out into `/etc/puppet` and thus controlled the Puppet Master. New EC2 instances (namely clusters of instances) were started from this server by running `bin/run-cluster.sh`, described below.

Puppet version: 2.6.14 (from Amazon yum repository).

What does it do
-------------

### Puppet Config

* puppet.conf: 
    * master:
        * certname: Hostname for CA certificate specified explicitly to use the public hostname of the instance (it would default to the internal one, visible only from the public EC2 - good enough for our needs but not e.g. for a VPC)
        * autosign: Automatically sign any certificate of a puppetd to make setup of new instances easier (insecure!)
    * agent (puppetd):
        * runinterval: Run every 10s instead of 30 min so that autodeploy of our updated binaries happens soon enough
* fileserver.conf:
    * Tell puppet to serve files located under `/etc/puppet/files` whenever a File in a puppet manifests has a `source` starting with `puppet:///files/`.
  
(The other \*.conf files in this folder could likely be just deleted.)

### Puppet Manifests

* Private config that we don't want to publish is in imported files in the `private/` subdirectory, see below.
* Key operations done by Puppet:
    * Install java (class `java`)
    * Create users, add them to the group `codecamp`, set their password, make it possible for them to log in via SSH without password by storing their public key (the long string from `~/.ssh/id_rsa.pub` on their machines) into `~/.ssh/authorized_keys`, enable them to use `sudo` without providing a password (not very secure) by copying `files/etc/sudoers` to the instances - see the classes `users` and `users-private` (see below) and `my-user.pp/myuser`
    * Install and start our software, i.e. `graft`; restart it if `/opt/graft.jar` gets updated (via `notify`).
* The newly started instances get the configuration of the `default` node, according to their `clustername` as described below. We've also declared a node for the Puppet Master itself so that we can run puppetd there to verify that all works.
* `my-user.pp` declares the resource  `myuser`, which can create a user and some necessary directories, set an allowed SSH key, and set the user's password (theoretically the ser resource should manage that on its own but it didn't work for me, likely due to some missing Ruby dependencies).

#### Private files

`private/private-users.pp` - creates users by calling `myuser` with a username and public SSH key, it contains something like:
    
    class users-private {
    	myuser { "jakub":
    		key => "<the long string from .ssh/id_rsa.pub>",
    }}

`private/private-vars.pp` - defines variables used later in `site.pp` and elsewhere, it contains something like:

    $user_psw='secret_password'

### Puppet Files

The files located here can be served to Puppet Agents. Some files, namely `files/opt/graft.jar` are not in the GitHub repo because they are produced by our CI server running at the Puppet Master instance.

### Scripts

The scripts in `bin/` are expected to be run by the root at the Puppet Master instance because root has access to all the EC2 command-line tools (thanks to having the apropriate private keys and setting the [EC2 environment variables](http://docs.amazonwebservices.com/AWSEC2/latest/UserGuide/setting-up-your-tools.html#set-aes-home) in  `.bashrc`).

* `run-cluster.sh` - see below
* `cluster-kill.sh` - terminate all the instances belonging to a cluster (i.e. running and using the key StarClusterCodeCamp12)
* `get-my-public-domainname.sh` - get the public hostname of the EC2 instance where this is executed; alternatively you could run `ec2-metadata  --public-hostname` (see `ec2-metadata  --help` and [EC2 metadata](http://docs.amazonwebservices.com/AWSEC2/latest/UserGuide/AESDG-chapter-instancedata.html))
* `run-puppetd-test.sh` - run puppetd locally at Puppet Master without applying the changes to check the configuration's validity; similarly `run-puppetd-interactive.sh` runs puppetd locally in the debug mode in foreground actually applying the changes
* `run-puppetmaster-interactive.sh` - used for troubleshooting puppetmaster, similar to `run-puppetd-interactive.sh`

#### Starting a Cluster with `run-cluster.sh`

The script `bin/run-cluster.sh` starts a number of instances via `ec2-run-instances` using a dedicated key-pair ("StarClusterCodeCamp12" - so that it's easy to recognize them in the output of `ec2-describe-instances`) and supplying instructions to Cloud Init via user data to bootstrap Puppet. It then waits for the instances to come up, which takes few minutes (and yet more minutes it takes for Puppet to initialize them).

The instances are initialized via [Cloud Init](https://help.ubuntu.com/community/CloudInit), which installs puppet, fetches and runs the `install-puppet.sh` script from the web (it fetches and installs the puppet.conf from GitHub to make the daemon run more frequently), and runs puppetd with the cluster name and Puppet Master hostname.

The script supports marking the instance(s) started as belonging to a particular "cluster" by setting the environment variable `FACTER_clustername` that is than available in puppet config as `$clustername` and can be thus used to include different, cluster-specific Puppet classes as you can see in manifests/site.pp in the node `default`. (You would usually also use a different key or assign the instance into a different security group so that you could easily see from the output of ec2-describe-instances what cluster they belong to. Alternatively, you could run `ec2-create-tags <instance ids> -t clusterName` and apply more clever parsing of the output to match instances with tags.)

### Limitations and insecurity

This setup was good enough for us but some parts of it cannot be recommanded in general because they are not secure, such as allowing autosign (=> any computer can download your puppet manifests and files).

I would also not recommand using Puppet for deploying applications, there are certainly more suitable tools for that.

Caveats
-------

All the communication between a Puppet Master and Puppet Agents (puppetd) uses certificates, cached both on the client and the server. If a hostname or st. else changes, this may become invalid making the communication impossible. A possible solution is to run both puppetmaster and puppetd in the debug mode (-d) to find out where they have their certificate caches (usually `/var/lib/puppet/ssl/`), stop them, delete these directories on both, and start them again. Notice that if puppetd's certificate request isn't signed by the server, the agent will not resend the request unless you delete it from its cache ([alternative solution](http://honglus.blogspot.com/2012/01/force-puppet-agent-to-regenerate.html)).

Note about VPC
-------------

It's expected that all EC2 instances run in the public cloud and not a Virtual Private Cloud (VPC). If using VPC then you must configure the security groups appropriately and enable the instances in the VPC to access the internet by running there a NAT gateway. (An instance in a VPC cannot access internet or be accessed from it unless it has been assigned an [Elastic IP](http://aws.amazon.com/articles/1346). If you try to start an instance first and assign an EIP to it afterwards, you'll get a race condition between the EIP and cloud init running on the instance (it needs internet to run).)
