package edu.com.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.com.model.Offer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements Serializer<Offer> {

    @Autowired
    private KafkaTemplate<String, Offer> kafkaOfferTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("offers_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    public void sendOffer(Offer offer) {
        kafkaOfferTemplate.send("offers_topic", offer);
        System.out.println("send");
    }

    @Override
    public byte[] serialize(String topic, Offer data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            System.out.println("Unable to serialize object");
            return null;
        }
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}

