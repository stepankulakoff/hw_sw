#!/bin/sh
set -eu
cd "$(dirname "$0")"

environment=${1:?Usage: ./run.sh dev|prod version}
version=${2:?Usage: ./run.sh dev|prod version}
case "$environment" in dev|prod) ;; *) exit 1 ;; esac
case "$version" in ''|*[!a-zA-Z0-9._-]*|.|..) exit 1 ;; esac

cd "releases/$environment/$version"
docker compose -p "homework-sw-$environment" up -d --wait
