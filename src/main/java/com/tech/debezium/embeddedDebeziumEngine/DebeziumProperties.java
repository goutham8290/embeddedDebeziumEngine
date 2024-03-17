package com.tech.debezium.embeddedDebeziumEngine;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "debezium")
@Getter
@Setter
public class DebeziumProperties {
    private String name;
    private String bootstrapServers;
    private String connectorClass;
    private Offset offset;
    private Database database;
    private String topicPrefix;
    private Table table;
    private Slot slot;
    private Plugin plugin;
    private Snapshot snapshot;

    @Getter
    @Setter
    public static class Offset {
        private String storage;
        private String topic;
        private int partitions;
        private int replicationFactor;
        private int flushIntervalMs;
    }

    @Getter
    @Setter
    public static class Database {
        private String hostname;
        private int port;
        private String user;
        private String password;
        private String dbname;
    }

    @Getter
    @Setter
    public static class Table {
        private String includeList;
    }

    @Getter
    @Setter
    public static class Slot {
        private String name;
    }

    @Getter
    @Setter
    public static class Plugin {
        private String name;
    }

    @Getter
    @Setter
    public static class Snapshot {
        private String mode;
    }
}
