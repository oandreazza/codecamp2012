node basenode {
   include cfn
}

# Note: /etc/puppet/autosign.conf contains "*.internal"
# and /etc/puppet/fileserver.conf contains "[modules]\n   allow *.internal
node /^.*internal$/ inherits basenode {
   case $cfn_roles {
      /wordpress/: { include wordpress }
  }
}
