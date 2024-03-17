package com.tech.debezium.embeddedDebeziumEngine;


import com.tech.debezium.embeddedDebeziumEngine.handler.PostgresEventHandler;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
public class EmbeddedDebeziumEngineApplication  {

	private static ExecutorService executorService;
	private static DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

	private static KafkaProducer producer;
	private static final Logger logger = LoggerFactory.getLogger(EmbeddedDebeziumEngineApplication.class);
	@Autowired
	private static DebeziumProperties debeziumProperties;



	public static void main(String[] args) {
		SpringApplication.run(EmbeddedDebeziumEngineApplication.class, args);
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());



		Configuration postgresDebeziumConfig = io.debezium.config.Configuration.create()
				.with("name", debeziumProperties.getName())
				.with("bootstrap.servers",debeziumProperties.getBootstrapServers())
				.with("connector.class", debeziumProperties.getConnectorClass())
				.with("offset.storage", debeziumProperties.getOffset())
				.with("offset.storage.topic", "debezium_tutorial_lsn")
				.with("offset.storage.partitions", "1")
				.with("offset.storage.replication.factor", "1")
				.with("offset.flush.interval.ms","6000")
				.with("database.hostname", "localhost")
				.with("database.port", "5432")
				.with("database.user", "debezium_user")
				.with("database.password", "debezium_pw")
				.with("database.dbname", "debezium_tutorial")
				.with("topic.prefix", "inventory")
				.with("table.include.list", "inventory.product")
				.with("slot.name","debezium_replication")
				.with("plugin.name","pgoutput")
				.with("snapshot.mode","initial")
				.build();

		PostgresEventHandler changeEventProcessor = new PostgresEventHandler(properties);
		debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
				.using(postgresDebeziumConfig.asProperties())
				.notifying(changeEventProcessor::handleChangeEvent)
				.build();



		executorService = Executors.newSingleThreadExecutor();
		executorService.execute(debeziumEngine);

		// Start the Debezium engine
		debeziumEngine.run();

	}


}



