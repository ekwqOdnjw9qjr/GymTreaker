package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Session> {

    @Query("SELECT s FROM Session s WHERE  s.coachingTime.id = ?1 AND s.dateOfTraining = ?2" +
            " AND s.coachingTime.dayOfWeek = ?3")
    List<Session> findConflictingSessions(Long coachingTimeId,LocalDate dateOfTraining,
                                          DayOfWeek dayOfWeek);

    List<Session> findSessionByUserId(Long id);

    @Query("SELECT s FROM Session s WHERE s.dateOfTraining = CAST(:date AS DATE)")
    List<Session> findSessionsByDate(@Param("date") LocalDate date);


}
