package com.tech.debezium.embeddedDebeziumEngine.model;

public record InventoryEvent(String operation, Integer id, String name, Double price){ }
