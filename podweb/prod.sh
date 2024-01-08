#!/bin/bash
set -e
set -o xtrace

npm run prod

./gradlew uberJar

sudo docker build -t podweb .

# Run command
# sudo docker run -p 7000:7000 podweb
