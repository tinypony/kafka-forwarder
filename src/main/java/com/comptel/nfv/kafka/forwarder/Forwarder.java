package com.comptel.nfv.kafka.forwarder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.json.JSONArray;
import org.json.JSONObject;

public class Forwarder {

	public static void main(String[] args) throws IOException {
		JSONObject config = getConfig("");
		KafkaProducer<String, String> producer = getProducer("10.0.0.98", 9092);
		List<PortForwarder> forwarders = new ArrayList<PortForwarder>();

		for(PortTopicBinding b : getBindings()) {
			forwarders.add(new PortForwarder(producer, b));
		}

		while(true) {
			for(PortForwarder pf:forwarders) {
				pf.forward();
			}
		}

	}

	private static JSONObject getConfig(String configFilePath) {
		JSONObject retval = new JSONObject();
		JSONArray producers = new JSONArray();

		return retval;
	}

	private static List<PortTopicBinding> getBindings() {
		List<PortTopicBinding> bindings = new ArrayList<PortTopicBinding>();
		bindings.add(new PortTopicBinding(9876, "latency"));
		return bindings;
	}

	private static KafkaProducer<String, String> getProducer(String kafkaHost, int kafkaPort) {
		 Properties props = new Properties();
		 props.put("bootstrap.servers", String.format("%s:%d", kafkaHost, kafkaPort));
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		 return new KafkaProducer<String, String>(props);
	}
}