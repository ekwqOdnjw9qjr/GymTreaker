package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fitnes.fitnestreaker.entity.CoachingTime;

import java.util.List;

public interface CoachingTimeRepository extends JpaRepository<CoachingTime, Long> {

    List<CoachingTime> findAllCoachingTimeByTrainerId(Long id);
}
