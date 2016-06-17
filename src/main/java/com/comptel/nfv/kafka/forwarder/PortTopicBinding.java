package com.comptel.nfv.kafka.forwarder;

public class PortTopicBinding {
	private int port;
	private String topic;

	public PortTopicBinding(int port, String topic) {
		this.setPort(port);
		this.setTopic(topic);
	}

	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
