package com.tech.debezium.embeddedDebeziumEngine.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.debezium.embeddedDebeziumEngine.EmbeddedDebeziumEngineApplication;
import com.tech.debezium.embeddedDebeziumEngine.model.ProductEvent;
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
import static jakarta.servlet.RequestDispatcher.ERROR_MESSAGE;

public class PostgresEventHandler {

    private final KafkaProducer<String, String> kafkaProducer;
    private static final String TOPIC_NAME = "product_stream";
    private static final String ERROR_MESSAGE = "Exception occurred during event handling";
    private final static String topic = "product_stream";
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
                Optional<ProductEvent> event = getProductEvent(sourceRecord, operation);
                if (event.isPresent()) {
                    String jsonEvent = objectMapper.writeValueAsString(event.get());
                    kafkaProducer.send(new ProducerRecord<>(TOPIC_NAME, jsonEvent));
                }
            } catch (Exception e) {
                logger.error(ERROR_MESSAGE, e);
            }
        }
    }

    private Optional<ProductEvent> getProductEvent(SourceRecord event,Envelope.Operation op){
        final Struct value = (Struct) event.value();
        final Struct key = (Struct) event.key();
        final Struct values = value.getStruct("after");
        if(values != null){
            String name = values.getString("name");
            Integer id = values.getInt32("id");
            Double price = (Double) values.get("price");

            return Optional.of(new ProductEvent(op.toString(), id, name, price));

        }else {
            return Optional.empty();
        }



    }
}
