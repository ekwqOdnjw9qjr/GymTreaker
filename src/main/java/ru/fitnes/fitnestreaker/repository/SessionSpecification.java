package ru.fitnes.fitnestreaker.repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.entity.Session;
import ru.fitnes.fitnestreaker.entity.Trainer;
import ru.fitnes.fitnestreaker.entity.User;
import ru.fitnes.fitnestreaker.entity.enums.SessionStatus;

@Repository
@RequiredArgsConstructor
public class SessionSpecification {

    public static Specification<Session> hasStatus(SessionStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("status"), "%" + status + "%");
        };
    }

    public static Specification<Session> hasTrainer(Trainer trainer) {
        return (root, query, criteriaBuilder) -> {
            if (trainer == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("trainer"), trainer);
        };
    }

}
