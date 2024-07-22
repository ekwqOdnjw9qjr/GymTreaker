package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
}
