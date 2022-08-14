# Kafka Connect File Stream

This repository contains a sample project that can be used to start off your own sink connector for Kafka Connect.

## Building the connector

The first thing you need to do to start using this connector is built it. To do that, you need to install the following dependencies:

- [Java 11+](https://openjdk.java.net/)
- [Apache Maven](https://maven.apache.org/)

After installing these dependencies, execute the following command:

```bash
mvn clean package
```

## Trying the connector

After building the connector, you can try it by using the Docker-based installation from this repository.

### 1 - Starting the environment

Start the environment with the following command:

```bash
docker-compose up
```

Wait until all containers are up so you can start the testing.

### 2 - Install the connector

Open a terminal to execute the following command:

```bash
curl -X POST -H "Content-Type:application/json" -d @connector-config.json http://localhost:8083/connectors
```

### 3 - Check the operation

Open a terminal to execute the following command:

```bash
docker exec -it kafka kafka-console-producer --bootstrap-server localhost:19092 --topic some-topic
```

Now write some messages and sent to Kafka topic, after that open another tab on terminal and execute these commands:

```bash
docker exec -it kafka-connect bash
```

```bash
cat messages.txt
```

## We are ready to write connectors 🚀
