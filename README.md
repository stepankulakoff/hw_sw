```sh
docker run -d --name homework-sw-zookeeper -p 127.0.0.1:42181:2181 zookeeper:3.9.5
```

```sh
docker start homework-sw-zookeeper
docker exec homework-sw-zookeeper zkServer.sh status
```

```sh
docker compose up -d --wait
mvn clean verify
```

```sh
java -jar rate-provider/target/rate-provider-1.0.0.jar --server.port=8091
```

```sh
java -jar rate-provider/target/rate-provider-1.0.0.jar --server.port=8092
```

```sh
java -jar rate-printer/target/rate-printer-1.0.0.jar
```

```sh
docker exec homework-sw-zookeeper zkCli.sh ls /services/currency
```

```sh
curl http://localhost:8091/rpc -H 'Content-Type: application/json' -d '{"jsonrpc":"2.0","method":"getRate","id":1}'
```

```sh
docker stop homework-sw-zookeeper
```

```sh
open http://localhost:29292
```

```sh
mvn -pl rate-printer clean verify
mvn -pl rate-provider clean verify
```

```sh
docker compose stop
```
