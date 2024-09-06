package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Membership;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {

    List<Membership> findMembershipsByUserId(Long id);

    @Query("SELECT m.id FROM Membership m WHERE m.user.id = :userId")
    List<Long> findMembershipsIdsByUserId(@Param("userId") Long userId);


}
