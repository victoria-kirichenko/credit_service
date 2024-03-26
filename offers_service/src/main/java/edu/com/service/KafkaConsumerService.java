package edu.com.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.com.model.Offer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements Deserializer<Offer> {

    @Autowired
    private OfferService offerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("offers_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @KafkaListener(topics = "offers_topic")
    public void listenOffers(Offer message) {
        offerService.saveRepository(message);
    }

    @Override
    public Offer deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data, "UTF-8"), Offer.class);
        } catch (Exception e) {
            System.out.println("Unable to deserialize message");
            return null;
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}


