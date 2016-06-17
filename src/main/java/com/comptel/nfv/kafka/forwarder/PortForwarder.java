package com.comptel.nfv.kafka.forwarder;
/**
 * File: Producer.java
**/

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Kafka Producer for OAM. This producer processes a csv instances file and
 * publishes to Kafka.
 *
**/
public class PortForwarder {

	private DatagramChannel localChannel;
	private ByteBuffer buffer;
	private final int LARGEST_DATAGRAM_SIZE = 65507;
	private KafkaProducer<String, String> producer;
	private PortTopicBinding binding;

    PortForwarder(KafkaProducer<String, String> producer, PortTopicBinding binding) throws IOException {
    	this.binding = binding;
    	this.localChannel = DatagramChannel.open();
        InetSocketAddress sAddr = new InetSocketAddress("localhost", binding.getPort());
        this.localChannel.bind(sAddr);
        this.buffer = ByteBuffer.allocate(LARGEST_DATAGRAM_SIZE);
        this.producer = producer;
    }

    public void forward() throws IOException {
    	buffer.clear();
    	SocketAddress source = this.localChannel.receive(this.buffer);

    	if(source == null) {
    		return;
    	}

    	String payload = this.buffer.toString();
    	this.producer.send( new ProducerRecord<String, String>(this.binding.getTopic(), "" + payload.hashCode(), payload) );
    }
}
