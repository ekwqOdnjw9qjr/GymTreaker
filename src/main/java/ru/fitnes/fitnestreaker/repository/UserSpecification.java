package ru.fitnes.fitnestreaker.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.fitnes.fitnestreaker.dto.UserDto;
import ru.fitnes.fitnestreaker.entity.User;

@Repository
@RequiredArgsConstructor
public class UserSpecification {

//    private final EntityManager entityManager;
//    public List<User> findAllByCriteria(UserDto userDto) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
//        List<Predicate> predicateList = new ArrayList<>();
//
//        Root<User> root = criteriaQuery.from(User.class);
//        if (userDto.getFirstName() != null) {
//            Predicate firstNamePredicate = criteriaBuilder
//                    .like(root.get("firstName"),"%" + userDto.getFirstName() + "%" );
//            predicateList.add(firstNamePredicate);
//        }if (userDto.getLastName() != null) {
//            Predicate lastNamePredicate = criteriaBuilder
//                    .like(root.get("lastName"),"%" + userDto.getLastName() + "%" );
//            predicateList.add(lastNamePredicate);
//        }if (userDto.getEmail() != null) {
//            Predicate emailPredicate = criteriaBuilder
//                    .like(root.get("email"),"%" + userDto.getEmail() + "%" );
//            predicateList.add(emailPredicate);
//        }
//        criteriaQuery.where(
//                criteriaBuilder.or(predicateList.toArray(new Predicate[0]))
//        );
//        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
//        return query.getResultList();
//        }
//    }
public static Specification<User> hasFirstName(String firstName) {
    return (root, query, criteriaBuilder) -> {
        if (firstName == null || firstName.isEmpty()) {
            return criteriaBuilder.conjunction(); // всегда истинно
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
