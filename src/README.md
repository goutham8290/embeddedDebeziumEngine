# Embedded Debezium Engine

This project demonstrates how to use the Debezium embedded engine to capture and stream database changes in real-time using Apache Kafka.

## Overview

The Debezium embedded engine allows you to embed Debezium directly into your Java application, enabling you to capture and process database changes without the need for external connectors or databases.

## Features

- **Real-time Change Data Capture (CDC):** Capture database changes in real-time.
- **Integration with Apache Kafka:** Stream captured changes to Apache Kafka topics.
- **Support for Various Databases:** Debezium supports various databases, including PostgreSQL, MySQL, SQL Server, MongoDB, and others.
- **Flexible Configuration:** Configure the embedded Debezium engine to capture changes from specific tables or databases.
- **Scalable and Fault-Tolerant:** Built on Apache Kafka, the streaming platform, providing scalability and fault-tolerance out of the box.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Kafka (optional, if you want to stream changes to Kafka)

### Installation

Clone the repository:

```bash
git clone https://github.com/goutham8290/embeddedDebeziumEngine.git

```
## Hands-on Tutorial

Check out our step-by-step guide on implementing Debezium with PostgreSQL:

[Debezium Implementation in PostgreSQL](https://dev.to/gouthamsayee/debezium-implementation-in-postgres-2fjd)

also checkout how Debezium works under the hood
[Debezium Introduction](https://dev.to/gouthamsayee/shipping-data-done-easy-3305)