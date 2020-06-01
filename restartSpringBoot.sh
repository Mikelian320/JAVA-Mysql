PORT="8080"
if [ $2 = "spring-boot" ]; then
    PORT="8088"
fi

kill -9 `cat pid.txt`
nohup java -jar $1 --server.port=${PORT} > nohup.log 2>&1 & echo $! > pid.txt