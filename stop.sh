#!/bin/sh

if [ "$#" -gt 0 ]; then
    case "$1" in dev|prod) ;; *) echo "Usage: ./stop.sh [dev|prod]"; exit 1 ;; esac
    for service in client service1 service2 grafana prometheus zookeeper; do
        containers=$(docker ps -q --filter "label=com.docker.compose.project=homework-sw-$1" \
            --filter "label=com.docker.compose.service=$service") || exit 1
        if [ -n "$containers" ]; then
            docker stop -t 60 $containers || exit 1
        fi
    done
    exit 0
fi

for port in 8094 8095 8096; do
    for pid in $(lsof -tiTCP:"$port" -sTCP:LISTEN); do
        command=$(ps -p "$pid" -o command=)
        case "$command" in
            *java*" -jar "*rate-provider/target/rate-provider-*.jar*|*java*" -jar "*rate-printer/target/rate-printer-*.jar*)
                if kill -TERM "$pid"; then
                    echo "Отправлен сигнал остановки: PID $pid, порт $port"
                fi
                ;;
            *)
                echo "Порт $port занят другим приложением — пропускаю"
                ;;
        esac
    done
done
