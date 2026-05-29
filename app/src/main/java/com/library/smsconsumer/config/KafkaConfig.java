package com.library.smsconsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaConfig {
    // ============================================
    // UPDATE THESE WITH YOUR CONFLUENT CLOUD CREDENTIALS
    // ============================================
    
    // Find in Confluent Cloud Console -> Cluster Settings -> Bootstrap server
    // Example: pkc-abc123.us-east-1.provider.confluent.cloud:9092
    public static final String BOOTSTRAP_SERVERS = "pkc-YOUR_REGION.confluent.cloud:9092";
    
    // Your Confluent Cloud API Key
    public static final String API_KEY = "YOUR_API_KEY";
    
    // Your Confluent Cloud API Secret
    public static final String API_SECRET = "YOUR_API_SECRET";
    
    // Your Kafka Topic Name
    public static final String TOPIC_NAME = "student-expiring-plans";
    
    // ============================================

    public static Properties getConsumerProperties() {
        Properties props = new Properties();
        
        // Confluent Cloud Configuration
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "PLAIN");
        props.put("sasl.jaas.config",
            "org.apache.kafka.common.security.plain.PlainLoginModule required " +
            "username=\"" + API_KEY + "\" password=\"" + API_SECRET + "\";");
        props.put("client.dns.lookup", "use_all_dns_ips");
        props.put("ssl.endpoint.identification.algorithm", "https");
        
        // Consumer Configuration
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "android-sms-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        
        return props;
    }
}
