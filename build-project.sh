#!/bin/bash
cd ./configserver && ./mvnw clean package -DskipTests && cd ..
cd ./eureka-server && ./mvnw clean package -DskipTests && cd ..
cd ./gateway && ./mvnw clean package -DskipTests && cd ..
cd ./notification && ./mvnw clean package -DskipTests && cd ..
cd ./order && ./mvnw clean package -DskipTests && cd ..
cd ./product && ./mvnw clean package -DskipTests && cd ..
cd ./user && ./mvnw clean package -DskipTests && cd ..