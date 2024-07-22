package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {
}
