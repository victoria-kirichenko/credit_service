package edu.com.service;

import edu.com.model.Offer;
import edu.com.model.Response;
import edu.com.model.User;
import edu.com.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@EnableScheduling
public class OfferGenerationService {
    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private UserService userService;

    private static final double MIN_INTEREST_RATE = 5.0;
    private static final double MAX_INTEREST_RATE = 15.0;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void generateMonthlyOffers() {

        Offer offer = generateMonthlyOffer();

        List<User> users = userService.findAllUsers();

        for (User user : users) {

            Response lastResponse = responseRepository.findResponsesByUserId(user.getId())
                    .stream().reduce((first, second) -> second).orElse(null);

            LocalDateTime now = LocalDateTime.now();
            boolean canGenerateOffer = false;

            if (lastResponse != null) {
                switch (lastResponse.getResponseType()) {
                    case 1:
                        canGenerateOffer = ChronoUnit.MONTHS.between(lastResponse.getResponseDate(), now) >= 1;
                        break;
                    case 2:
                        canGenerateOffer = ChronoUnit.MONTHS.between(lastResponse.getResponseDate(), now) >= 3;
                        break;
                    case 3:
                        canGenerateOffer = false;
                        break;
                }
            } else {
                canGenerateOffer = true;
            }

            if (canGenerateOffer) {
                offer.setUser(user);
                kafkaProducerService.sendOffer(offer);
            }
        }

    }

    private Offer generateMonthlyOffer() {
        double interestRate = ThreadLocalRandom.current().nextDouble(MIN_INTEREST_RATE, MAX_INTEREST_RATE);
        String formattedInterestRate = String.format("%.2f", interestRate);

        Offer offer = new Offer();
        offer.setContent("Take out a loan at " + formattedInterestRate + "% annual interest.");
        offer.setCreationDate(LocalDateTime.now());

        return offer;
    }
}

