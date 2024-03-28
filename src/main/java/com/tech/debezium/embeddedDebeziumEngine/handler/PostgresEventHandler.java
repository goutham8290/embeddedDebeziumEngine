package com.tech.debezium.embeddedDebeziumEngine.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.debezium.embeddedDebeziumEngine.model.InventoryEvent;
import io.debezium.data.Envelope;
import io.debezium.engine.RecordChangeEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;

import static io.debezium.data.Envelope.FieldName.OPERATION;


public class PostgresEventHandler {

    private final KafkaProducer<String, String> kafkaProducer;
    private static final String TOPIC_NAME = "bookstore_inventory_stream";
    private static final String ERROR_MESSAGE = "Exception occurred during event handling";
    private static final Logger logger = LoggerFactory.getLogger(PostgresEventHandler.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    public PostgresEventHandler(Properties kafkaProperties) {
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties);
    }


    public void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

        if (sourceRecordChangeValue != null) {
            try {
                Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));
                Optional<InventoryEvent> event = getProductEvent(sourceRecord, operation);
                if (event.isPresent()) {
                    String jsonEvent = objectMapper.writeValueAsString(event.get());
                    kafkaProducer.send(new ProducerRecord<>(TOPIC_NAME, jsonEvent));
                }
            } catch (Exception e) {
                logger.error(ERROR_MESSAGE, e);
            }
        }
    }

    private Optional<InventoryEvent> getProductEvent(SourceRecord event, Envelope.Operation op) {
        final Struct value = (Struct) event.value();
        Struct values = null;

        // Since the operations for CREATE and READ are identical in handling,
        // they are combined into a single case.
        switch (op) {
            case CREATE:
            case READ:
            case UPDATE: // Handle UPDATE similarly to CREATE and READ, but you're now aware it's an update.
                values = value.getStruct("after");
                break;
            case DELETE:
                values = value.getStruct("before");
                if (values != null) {
                    Integer id = values.getInt32("id");
                    return Optional.of(new InventoryEvent(op.toString(), id, null, null));
                } else {
                    return Optional.empty();
                }

            default:
                // Consider whether you need a default case to handle unexpected operations
                return Optional.empty();
        }

        if (values != null) {
            String name = values.getString("name");
            Integer id = values.getInt32("id");
            Double price = (Double) values.get("price");
            return Optional.of(new InventoryEvent(op.toString(), id, name, price));
        } else {
            return Optional.empty();
        }

    }
}
