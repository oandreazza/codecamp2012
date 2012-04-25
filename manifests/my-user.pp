        define myuser($key) {
                $username = $title
                user { $username:
                        managehome => true,
                        home   => "/home/${username}",
                        groups => "codecamp",
                        require => Group["codecamp"],
                }

                exec { "set-user-password-$username":
                        command => "echo -e \"$user_psw\\n$user_psw\" | passwd --stdin $username",
                        require => User[$username],
                }

                file  { "/home/$username" :
                        ensure => directory ,
                        owner => $username;

                        "/home/$username/.ssh" :
                        ensure => directory ,
                        mode => 700 ,
                        owner => $username ,
                }

                ssh_authorized_key { $username:
                        key => $key,
                        user => $username,
                        type => "rsa",
                }
        }
