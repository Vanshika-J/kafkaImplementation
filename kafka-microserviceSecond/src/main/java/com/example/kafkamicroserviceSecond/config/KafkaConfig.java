package com.example.kafkamicroserviceSecond.config;

import com.example.kafkamicroserviceSecond.Model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaConfig {
    private  static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private KafkaTemplate<String, String> kafkaTemplate;
    @KafkaListener(topics = Constants.LOCATION_UPDATE_TOPIC, groupId = Constants.GROUP_ID)
    public void updateLocation(String message){
        LOGGER.info("Received message: {}", message);


        // Deserialize the received message back to EventType object
        try {
            EventType eventType = deserializeEvent(message);
            if (eventType != null) {
                processEvent(eventType);
            } else {
                sendToDLQ(message);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing the message: {}", e.getMessage());
            sendToDLQ(message);
        }
    }
    private  void  sendToDLQ(String message) {
        this.kafkaTemplate.send(Constants.DLQ_TOPIC, message);
    }
    public  EventType deserializeEvent(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, EventType.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error occurred while serializing EventType object: {}", e.getMessage());
            // Send the failed message to DLQ
            sendToDLQ(jsonString);
            return null;
        }
    }


    private void processEvent(EventType eventType) {
        LOGGER.info("Processing EventType: {}", eventType);

    }


}
