#!/bin/sh

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
