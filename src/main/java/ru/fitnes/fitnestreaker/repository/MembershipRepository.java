package ru.fitnes.fitnestreaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.dto.response.MembershipResponseDto;
import ru.fitnes.fitnestreaker.entity.Membership;

import java.util.List;
import java.util.Set;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {

    Set<Membership> findMembershipsByUserId(Long id);

    List<Long> findMembershipsListByUserId(Long id);

}
