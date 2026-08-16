package djnd.happy.farm.repository;

import djnd.happy.farm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";
    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findOneByLogin(String login);
    Optional<User> findOneByEmail(String email);


    List<User> findAllByActivatedIsFalseAndActivationKeyNotNullAndCreatedDateBefore(Instant createdDateBefore);


}
