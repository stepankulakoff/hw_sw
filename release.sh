#!/bin/sh
set -eu
cd "$(dirname "$0")"

environment=${1:?Usage: ./release.sh dev|prod version}
version=${2:?Usage: ./release.sh dev|prod version}
case "$environment" in dev|prod) ;; *) exit 1 ;; esac
case "$version" in ''|*[!a-zA-Z0-9._-]*|.|..) exit 1 ;; esac

test -f rate-provider/target/rate-provider-1.0.0.jar
test -f rate-printer/target/rate-printer-1.0.0.jar
directory="releases/$environment/$version"
mkdir -p "releases/$environment"
mkdir "$directory"
cp rate-provider/target/rate-provider-1.0.0.jar "$directory/rate-provider.jar"
cp rate-printer/target/rate-printer-1.0.0.jar "$directory/rate-printer.jar"
cp deploy/compose.yaml "$directory/compose.yaml"
cp "deploy/$environment.env" "$directory/.env"
cp -R monitoring "$directory/monitoring"
sed -e 's/host.docker.internal:8094/service1:8080/g' \
    -e 's/host.docker.internal:8095/service2:8080/g' \
    -e 's/host.docker.internal:8096/client:8080/g' \
    monitoring/prometheus.yml > "$directory/monitoring/prometheus.yml"
echo "$directory"
