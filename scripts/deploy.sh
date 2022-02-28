#!/bin/bash

DEST=/home/ec2-user/app/codeDeploy
PROJECT_NAME=pocketmark

cd $DEST


# stdout 기록할 파일만들기
rm bash_out.txt
touch bash_out.txt


CURRENT_PID=$(pgrep -fl pocket | grep java | awk '{print $1}')

echo ">>> 현재 구동 중 어플리케이션 PID : $CURRENT_PID" >> bash_out.txt

if [ -z "$CURRENT_PID"] 
then
    echo ">>> 현재 구동 중 어플리케이션 없음" >> bash_out.txt
else
    echo ">>> 구동 중인 프로세스 PID $CURRENT_PID 를 종료합니다." >> bash_out.txt
    echo ">>> kill -15 $CURRENT_PID" >> bash_out.txt
    kill -15 $CURRENT_PID >> bash_out.txt 2>&1
    sleep 5
fi

ll >> bash_out.txt 2>&1

JAR_NAME=$(ls -tr *.jar | tail -n 1)

echo ">>> JAR_NAME = $JAR_NAME" >> bash_out.txt

chmod +x $JAR_NAME >> bash_out.txt 2>&1

echo ">>> $JAR_NAME 을 실행합니다. " >> bash_out.txt

nohup java -jar $JAR_NAME > nohup.out 2>&1 &
