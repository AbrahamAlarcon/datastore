#! /bin/bash

MAVEN_HOME=C:\\tools\\apache-maven-3.5.0-bin\\apache-maven-3.5.0
JAVA_HOME=C:\\tools\\java\\jdk1.8.0_131


# Add ant to path
PATH=$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH
export MAVEN_HOME JAVA_HOME PATH

java -version
mvn -version

SKIP=$1
mvn clean package $SKIP

if [ $? = 0 ] ; then
  echo "Build successful"
else
  echo "Build failed"
fi

./local_ssl_build.sh

ENV=dev
APP_NAME=datastore
APPLICATION_EXECUTABLE=target/$APP_NAME-1.0.0.jar

old_pid=$(cat app.pid)
old_pid=$(ps aux|grep $old_pid)

if [[ "$old_pid" != ""  ]]; then
   echo "Application" $APPLICATION_EXECUTABLE "is still running PID " $(cat app.pid)
   exit -1
fi

#JVM attributes
JAVA_HEAP_XMX=100m
JAVA_HEAP_XMS=100m
CMS_INIT_OCCUPANCY_FRACTION=70
SERVER_PORT=8000
DEBUG_PORT=5000
LOGGING_PATH=C:/logs/$APP_NAME
GC_LOG=$LOGGING_PATH/gc-%t.log
GC_CLIENT_GC_INTERVAL=600000
GC_NUM_OF_GCLOG_FILES=5
GC_LOG_FILE_SIZE=10M
HEAP_DUMP_FILE=$LOGGING_PATH/dump-%t.hprof
INETADDR_TTL=60
PEERS="-Dpeer1=localhost:8761 -Dpeer2=localhost:8762"
WEATHER_KEY=066631e4c9e41f5a

#nohup
#java -Dapp.name=${APP_NAME} -Dweather.key=${WEATHER_KEY} -Dspring.profiles.active=${ENV} ${PEERS} -Xmx${JAVA_HEAP_XMX} -Xms${JAVA_HEAP_XMS} -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=${CMS_INIT_OCCUPANCY_FRACTION} -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -Xloggc:${GC_LOG} -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=${GC_NUM_OF_GCLOG_FILES} -XX:GCLogFileSize=${GC_LOG_FILE_SIZE} -Dsun.rmi.dgc.client.gcInterval=${GC_CLIENT_GC_INTERVAL} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${HEAP_DUMP_FILE} -Dsun.net.inetaddr.ttl=${INETADDR_TTL} -Dserver.port=${SERVER_PORT} -Xrunjdwp:server=y,transport=dt_socket,address=${DEBUG_PORT},suspend=n -jar ${APPLICATION_EXECUTABLE}
#>/dev/null 2>&1 </dev/null &

java -Dapp.name=${APP_NAME} -Dweather.key=${WEATHER_KEY} -Dspring.profiles.active=${ENV} ${PEERS} -Xmx${JAVA_HEAP_XMX} -Xms${JAVA_HEAP_XMS} -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=${CMS_INIT_OCCUPANCY_FRACTION} -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -Dsun.net.inetaddr.ttl=${INETADDR_TTL} -Dserver.port=${SERVER_PORT} -Xrunjdwp:server=y,transport=dt_socket,address=${DEBUG_PORT},suspend=n -jar ${APPLICATION_EXECUTABLE}