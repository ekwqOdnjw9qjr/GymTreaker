package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Session;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Session> {

    @Query("SELECT s FROM Session s WHERE s.trainer.id = ?1 AND s.coachingTime.id = ?2 AND s.dateOfTraining = ?3" +
            " AND s.coachingTime.dayOfWeek = ?4")
    List<Session> findConflictingSessions(Long trainerId, Long coachingTimeId,LocalDate dateOfTraining,
                                          DayOfWeek dayOfWeek);


    List<Session> findSessionByUserId(Long id);
}
