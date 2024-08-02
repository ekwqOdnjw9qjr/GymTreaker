package ru.fitnes.fitnestreaker.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.entity.User;

@Repository
@RequiredArgsConstructor
public class UserSpecification {

public static Specification<User> hasFirstName(String firstName) {
    return (root, query, criteriaBuilder) -> {
        if (firstName == null || firstName.isEmpty()) {
            return criteriaBuilder.conjunction();
        }
        return criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
    };
}
    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
        };
    }
    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }
}
