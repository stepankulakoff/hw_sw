```sh
docker compose up -d --wait pact-broker
mvn clean verify
```

```sh
./release.sh dev v1
./release.sh prod v1
```

```sh
./run.sh dev v1
./run.sh prod v1
```

```sh
open http://localhost:18300/d/homework-sw
open http://localhost:28300/d/homework-sw
curl -u admin:homework http://localhost:18300/api/health
open http://localhost:18090/targets
open http://localhost:28090/targets
open http://localhost:29292
```

```sh
curl http://localhost:18094/rpc -H 'Content-Type: application/json' -H 'X-Client-Id: manual' -d '{"jsonrpc":"2.0","method":"getRate","id":1}'
curl http://localhost:28094/rpc -H 'Content-Type: application/json' -H 'X-Client-Id: manual' -d '{"jsonrpc":"2.0","method":"getRate","id":1}'
curl http://localhost:18094/actuator/prometheus
curl http://localhost:18095/actuator/prometheus
curl http://localhost:18096/actuator/prometheus
```

```sh
docker logs -f homework-sw-dev-client-1
```

```sh
docker logs -f homework-sw-prod-service1-1
```

```sh
./stop.sh dev
./stop.sh prod
```

```sh
mvn clean verify
./release.sh dev v2
./run.sh dev v2
```

```sh
./run.sh dev v1
```
