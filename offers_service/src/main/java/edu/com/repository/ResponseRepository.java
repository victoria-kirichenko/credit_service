package edu.com.repository;

import edu.com.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    void deleteByResponseTypeAndResponseDateBefore(int type, LocalDateTime date);

    @Query("SELECT r FROM Response r JOIN r.offer o WHERE o.user.id = :userId")
    List<Response> findResponsesByUserId(@Param("userId") UUID userId);
}

