package com.tech.debezium.embeddedDebeziumEngine.model;

public record ProductEvent(String opreation, Integer id, String name, Double price){ }
