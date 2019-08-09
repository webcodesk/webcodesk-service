/opt/jdk-11.0.2/bin/java -server -Dserver.port=9090 -Xmx500m -Xms500m -XX:-UseParallelOldGC -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5050 -jar ./target/webcodesk-service-1.0.jar
