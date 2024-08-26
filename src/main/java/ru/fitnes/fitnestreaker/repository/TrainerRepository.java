package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Trainer;

import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {

    Set<Trainer> findTrainerByUserId(Long id);

    Trainer findByUserId(Long id);

}
