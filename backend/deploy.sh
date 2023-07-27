#!/bin/bash

JAR_FILE_PATH="/home/ubuntu/backend/build"
echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(sudo lsof -i tcp:8080 | awk 'NR!=1 {print$2}')

echo "현재 구동중인 애플리케이션pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    sudo kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"

nohup sudo -E java -jar $JAR_FILE_PATH/mapbefine.jar --spring.profiles.active=$profile >> $JAR_FILE_PATH/deploy.log 2> $JAR_FILE_PATH/deploy-err.log &
