package com.tech.debezium.embeddedDebeziumEngine.handler;

public record KafkaProductPayload(String operation, Integer id , Integer price , String name) {
}
