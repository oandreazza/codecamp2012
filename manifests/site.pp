import 'private/private-vars.pp'
import 'my-user.pp'
import 'private/private-users.pp'

# Set defaults
Exec { path => [ "/bin/", "/sbin/" , "/usr/bin/", "/usr/sbin/" ] }

# Global vars
	$java_pkg = $operatingsystem ? {
		ubuntu => "openjdk-6-jre-headless",
		default => "java-1.6.0-openjdk.x86_64"
	}
### CLASSES
class base_sw {
}
class java {
      package { $java_pkg:
          ensure => installed
      }
}

class users {

	file { "/etc/sudoers":
		owner => root,
		group => root,
		mode => 440,
		source => "puppet:///files/etc/sudoers"
	}	

	group { "codecamp": ensure => present }

	include users-private
}

### CLASSES

class database {
	exec { "/bin/touch /database": }

	file {'/opt/graft.jar':
                source  => 'puppet:///files/opt/graft-0.0.1-SNAPSHOT.jar',
                ensure => 'file',
		notify => Service['graft'],
        }

        file { "/etc/init.d/graft-run":
         ensure => file,
         owner => root,
         group => root,
         mode => 0755,
         source => "puppet:///files/etc/init.d/graft-run",
    	}

	file { "/etc/init.d/graft":
         ensure => file,
         owner => root,
         group => root,
         mode => 0755,
         source => "puppet:///files/etc/init.d/graft",
    }

    file { "/etc/rc0.d/S99graft":
      target =>  "/etc/init.d/graft",
      ensure => symlink
    }
	service { "graft":
        	start => '/etc/init.d/graft start',
        	stop => '/etc/init.d/graft stop',
		restart => '/etc/init.d/graft restart',
        	ensure => running,
		require => [ File['/opt/graft.jar'], File['/etc/init.d/graft-run'], Package[$java_pkg] ],
    }
}

class testclient {
	exec { "/bin/touch /testclient": }

	file {'/opt/apache-jmeter.tgz':
        	source  => 'puppet:///files/opt/apache-jmeter-2.6.tgz',
        	ensure => 'file',
      	}

	exec { "/bin/tar xzf /opt/apache-jmeter.tgz":
		cwd => "/opt",
		creates => "/opt/apache-jmeter",
		require => File["/opt/apache-jmeter.tgz"],
	}

}

###
### NODES
###
node basenode {
	include base_sw
	include java
	include users
}

node 'lucid32' inherits basenode {
	package { "elinks": ensure => installed }
}

# Puppet Master self - for testing
node 'ip-10-56-71-201.eu-west-1.compute.internal' inherits basenode {
	include database
}

# DB Nodes
node default inherits basenode {
	case $clustername {
		'database': { include database }
		'testclient': { include testclient }
		default: { 
			exec { "/bin/touch /nocluster-$clustername": }
		}
	}
}
