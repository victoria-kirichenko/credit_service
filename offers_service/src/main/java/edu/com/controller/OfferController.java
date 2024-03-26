package edu.com.controller;

import edu.com.model.Offer;
import edu.com.model.Response;
import edu.com.service.OfferService;
import edu.com.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private ResponseService responseService;

    @GetMapping("/show_offers")
    public ResponseEntity<List<Offer>> showOffersForUser(@RequestParam("userId") UUID userId) {
        try {
            List<Response> responses = responseService.findResponsesByUserId(userId);
            List<Offer> offers = offerService.getAvailableOffersForUser(responses);

            if (offers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(offers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/accept_offer")
    public ResponseEntity<?> acceptOffer(@RequestParam("offerId") UUID offerId) {
        try {
            responseService.saveResponse(offerId, 2);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decline_offer")
    public ResponseEntity<?> declineOffer(@RequestParam("offerId") UUID offerId) {
        try {
            responseService.saveResponse(offerId, 1);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ask_later_offer")
    public ResponseEntity<?> askLaterOffer(@RequestParam("offerId") UUID offerId) {
        try {
            responseService.saveResponse(offerId,3);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

