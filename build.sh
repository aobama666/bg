export PATH=/usr/lib/maven/apache-maven-3.3.9/bin:$PATH
mvn -P test clean package -Dmaven.test.skip=true