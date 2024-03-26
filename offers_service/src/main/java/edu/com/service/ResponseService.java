package edu.com.service;

import edu.com.model.Offer;
import edu.com.model.Response;
import edu.com.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private OfferService offerService;

    public void saveResponse(UUID offerId, int responseType) {
        Offer offer = offerService.getOffer(offerId);

        Response response = new Response();

        response.setOffer(offer);
        response.setResponseDate(LocalDateTime.now());
        response.setResponseType(responseType); // DECLINED - 1, ACCEPTED - 2, ASK LATER - 3

        responseRepository.save(response);
    }

    @Scheduled(cron = "0 0 1 * * ?") // каждый день в 01:00
    public void deleteByResponseTypeAndResponseDateBefore() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        responseRepository.deleteByResponseTypeAndResponseDateBefore(1, sixMonthsAgo); // DECLINE
        responseRepository.deleteByResponseTypeAndResponseDateBefore(2, sixMonthsAgo); // ACCEPTED
    }

    public List<Response> findResponsesByUserId(UUID userId) {
        return responseRepository.findResponsesByUserId(userId);
    }
}

