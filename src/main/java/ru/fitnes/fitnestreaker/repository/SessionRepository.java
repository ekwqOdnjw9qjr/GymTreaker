package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
}
