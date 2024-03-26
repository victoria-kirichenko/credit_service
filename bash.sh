#!/bin/bash

(cd authorize_service && ./gradlew bootRun) &

(cd generation_offers_service && ./gradlew bootRun) &

(cd offers_service && ./gradlew bootRun) &

wait