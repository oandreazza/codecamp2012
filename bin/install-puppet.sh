wget http://rubyforge.org/frs/download.php/76032/rubygems-1.8.23.tgz
tar xzf rubygems-1.8.23.tgz
ruby rubygems-1.8.23/setup.rb
gem install --no-ri --no-rdoc puppet -v 2.6.15
mkdir -p /etc/puppet
wget -O /etc/puppet/puppet.conf https://raw.github.com/iterate/codecamp2012/puppet/puppet.conf
