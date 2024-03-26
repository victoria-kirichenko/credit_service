package edu.com.repository;

import edu.com.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    Offer getReferenceById(UUID offerId);
}

