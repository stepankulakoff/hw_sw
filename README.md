```sh
docker compose up -d --wait
mvn clean verify
```

```sh
java -jar rate-provider/target/rate-provider-1.0.0.jar --server.port=8094
```

```sh
java -jar rate-provider/target/rate-provider-1.0.0.jar --server.port=8095
```

```sh
java -jar rate-printer/target/rate-printer-1.0.0.jar
```

```sh
open http://localhost:23000/d/homework-sw
curl -u admin:homework http://localhost:23000/api/health
open http://localhost:29090/targets
open http://localhost:29292
```

```sh
curl http://localhost:8094/rpc -H 'Content-Type: application/json' -H 'X-Client-Id: manual' -d '{"jsonrpc":"2.0","method":"getRate","id":1}'
curl http://localhost:8094/actuator/prometheus
curl http://localhost:8095/actuator/prometheus
curl http://localhost:8096/actuator/prometheus
curl http://localhost:8094/actuator/info
```

```sh
docker compose exec zookeeper zkCli.sh ls /services/currency
```

```sh
mvn -pl rate-printer clean verify
mvn -pl rate-provider clean verify
```

```sh
./stop.sh
docker compose stop
```
