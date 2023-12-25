package com.example.kafkamicroserviceSecond.KafkaProcess;

import com.example.kafkamicroserviceSecond.config.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {
    @Bean
    public NewTopic topic(){

        return TopicBuilder.name(Constants.DLQ_TOPIC).build();
    }
}

