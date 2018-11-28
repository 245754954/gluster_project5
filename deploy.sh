#!/bin/sh

mvn clean package -DskipTests

# client
cp ./hycloud-client/target/hycloud-client.jar ~/Work/run/client/
cp ./hycloud-client/client.properties ~/Work/run/client/

# server
scp ./hycloud-server/server.properties master:/home/hadoop/run/server/
scp ./hycloud-server/target/hycloud-server-0.0.1-SNAPSHOT.jar master:/home/hadoop/run/server/hycloud-server.jar

# hadoop
scp hycloud-hadoop/namenode.properties master:/home/hadoop/run/hadoop/
scp hycloud-hadoop/target/verify-background.jar master:/home/hadoop/run/hadoop/

# tools
scp hycloud-hadoop/namenode.properties master:/home/hadoop/run/tools/
scp hycloud-hadoop/target/destory.jar master:/home/hadoop/run/tools/
