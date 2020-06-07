#!/usr/bin/env bash

set -e
rm -f target/*.jar
mvn package
mkdir -p target/docker
cp target/bool-expression-matcher-service.jar target/docker/.
docker-compose build
