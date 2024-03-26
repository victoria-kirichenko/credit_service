package edu.com.service;

import edu.com.model.Offer;
import edu.com.model.Response;
import edu.com.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getAvailableOffersForUser(List<Response> responses) {

        Set<UUID> respondedOfferIds = responses.stream()
                .filter(response -> response.getResponseType() != 3) // !=  ASK_LATER
                .map(response -> response.getOffer().getId())
                .collect(Collectors.toSet());

        List<Offer> offers = offerRepository.findAll();

        if (!respondedOfferIds.isEmpty()) {
            List<Offer> availableOffers = offers.stream()
                    .filter(offer -> !respondedOfferIds.contains(offer.getId()))
                    .collect(Collectors.toList());
            return availableOffers;
        }

        return offers;
    }
    public void saveRepository(Offer offer) {
        offerRepository.save(offer);
    }

    public Offer getOffer(UUID offerId) {
        return offerRepository.getReferenceById(offerId);
    }

}

