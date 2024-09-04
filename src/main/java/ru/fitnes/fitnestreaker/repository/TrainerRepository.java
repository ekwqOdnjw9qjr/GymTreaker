package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {

    Trainer findTrainerByCreatedBy(Long id);
    @Query("SELECT u FROM User u WHERE u.trainer.id = ?1")
    List<User> findUserByTrainerIdInUserTable(Long id);
}
