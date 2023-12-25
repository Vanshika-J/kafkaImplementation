package com.example.kafkamicroserviceFirst.Service;

import com.example.kafkamicroserviceFirst.config.Constants;
import com.example.kafkamicroserviceFirst.Model.EventType; // Import your EventType class
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(KafkaService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean sendMessage(EventType eventType) {
        try {
            String eventAsString = objectMapper.writeValueAsString(eventType);
            this.kafkaTemplate.send(Constants.LOCATION_TOPIC_NAME, eventAsString);
            return true;
        } catch (JsonProcessingException e) {
            logger.error("Error occurred while serializing EventType object: {}", e.getMessage());
            return false;
        }
    }
}
