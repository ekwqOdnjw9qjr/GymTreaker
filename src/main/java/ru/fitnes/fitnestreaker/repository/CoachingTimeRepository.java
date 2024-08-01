package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fitnes.fitnestreaker.entity.CoachingTime;

public interface CoachingTimeRepository extends JpaRepository<CoachingTime, Long> {
}
