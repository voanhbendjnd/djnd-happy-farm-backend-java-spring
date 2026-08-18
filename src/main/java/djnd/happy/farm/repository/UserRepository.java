package djnd.happy.farm.repository;

import djnd.happy.farm.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    /*
    * Email must be lowed case before call method
    * */
    @EntityGraph(attributePaths = {"authorities"})
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    /*
    * Login must be lowed case before call this method
    * */
    @EntityGraph(attributePaths = {"authorities"})
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);
    @Modifying
    @Query(value = "update User u set u.sessionId = :sessionId where u.id = :userId")
    int updateSessionIdById(@Param("userId") Long userId, @Param("sessionId") String sessionId);

    @Modifying
    @Query(value = "update User u set u.refreshToken = :newRefreshToken where u.id = :userId")
    int updateRefreshTokenById(@Param("userId") Long userId, @Param("newRefreshToken") String newRefreshToken);
}
