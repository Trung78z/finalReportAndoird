### Install jdk 21
wget https://download.oracle.com/java/21/archive/jdk-21.0.6_linux-x64_bin.deb
sudo apt install ./jdk-21.0.6_linux-x64_bin.deb
export JAVA_HOME=/usr/lib/jvm/jdk-21.0.6-oracle-x64
export PATH=$JAVA_HOME/bin:$PATH
