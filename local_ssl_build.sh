#! /bin/bash

JAVA_HOME=${JAVA_HOME:-C:\\tools\\java\\jdk1.8.0_131}

VERSION=1.0.0
CERT_NAME=datastore
PASSWORD=datastore123
CACERTS=$JAVA_HOME\\jre\\lib\\security\\cacerts
LOCAL_VM=$(hostname -f)

# Add ant to path
PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME PATH

java -version

EXISTS=$(keytool -list -keystore $CACERTS -storepass changeit | grep $CERT_NAME.$VERSION)

if [[ "$EXISTS" != ""  ]]; then
   echo "Certificate" $CERT_NAME.$VERSION "already exist "
   zip -u target/$CERT_NAME-1.0.0.jar "$CERT_NAME.$VERSION.jks.dev"
   exit 0
fi

# adding Subject Alternative Name and Validity by 10 years (365x10)
keytool -genkey -keyalg RSA -alias "$CERT_NAME.$VERSION" -dname "cn=$CERT_NAME.$VERSION, ou=abraham.alarcon-org, o=org" -ext SAN=dns:$LOCAL_VM,dns:localhost,dns:node1-eureka.abrahamalarcon.com,dns:node2-eureka.abrahamalarcon.com -keystore "$CERT_NAME.$VERSION.jks" -storepass $PASSWORD -validity 3650 -keysize 2048 -keypass $PASSWORD

keytool -export -alias "$CERT_NAME.$VERSION" -file "$CERT_NAME.$VERSION.cer" -keystore "$CERT_NAME.$VERSION.jks" -storepass $PASSWORD

keytool -import -trustcacerts -noprompt -file "$CERT_NAME.$VERSION.cer" -alias "$CERT_NAME.$VERSION" -keystore $CACERTS -storepass changeit

keytool -list -keystore $CACERTS -storepass changeit | grep $CERT_NAME

mv "$CERT_NAME.$VERSION.jks" "$CERT_NAME.$VERSION.jks.dev"
zip -u target/$CERT_NAME-1.0.0.jar "$CERT_NAME.$VERSION.jks.dev"

