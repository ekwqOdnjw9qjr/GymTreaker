package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

//    @Query("SELECT s FROM Session s WHERE s.trainingDateAndTime = ?1 AND s.trainer")
//    Optional<Session> findByUniqueAttributes(LocalDateTime trainingDateAndTime);
}
