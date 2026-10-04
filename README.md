```sh
mvn clean package
```

```sh
java -jar rate-provider/target/rate-provider-1.0.0.jar
```

```sh
java -jar rate-printer/target/rate-printer-1.0.0.jar
```

```sh
curl http://localhost:8091/rpc -H 'Content-Type: application/json' -d '{"jsonrpc":"2.0","method":"getRate","id":1}'
```
