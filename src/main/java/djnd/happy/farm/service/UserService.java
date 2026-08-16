package djnd.happy.farm.service;

import djnd.happy.farm.config.Constants;
import djnd.happy.farm.domain.Authority;
import djnd.happy.farm.domain.User;
import djnd.happy.farm.repository.AuthorityRepository;
import djnd.happy.farm.repository.UserRepository;
import djnd.happy.farm.security.AuthoritiesConstants;
import djnd.happy.farm.service.dto.UserDTO;
import io.netty.util.Constant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final CacheManager cacheManager;
    final AuthorityRepository authorityRepository;
    final PasswordEncoder passwordEncoder;
    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
           boolean removed = this.removeNonActivatedUser(existingUser);
           if(!removed) {
               throw new AccountAlreadyUsedException("Login name already exist!");
           }
        });
        boolean isContainEmail = userDTO.getEmail() != null;
        if(isContainEmail) {
            userRepository.findOneByEmail(userDTO.getEmail().toLowerCase()).ifPresent(existingUser -> {
                boolean removed = this.removeNonActivatedUser(existingUser);
                if(!removed) {
                    throw new AccountAlreadyUsedException("Email already exist!");
                }
            });
        }

        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setName(userDTO.getName());
        if(isContainEmail){
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setLangKey(userDTO.getLangKey() !=  null ? userDTO.getLangKey() : Constants.DEFAULT_LANGUAGE);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        return newUser;
    }
    /*
    * return false -> account with login or email already exist
    * */
    private boolean removeNonActivatedUser(User existingUser){
        if(existingUser.getActivated()){
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;

    }
    /*
    * ensure account if delete in database -> role authorities in RAM also clear
    * */
    private void clearUserCaches(User user){
        var cacheByLogin = cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE);

        if(cacheByLogin != null){
            cacheByLogin.evictIfPresent(user.getLogin().toLowerCase());
            if(user.getEmail() != null){
                var cacheByEmail = cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE);
                if(cacheByEmail != null){
                    cacheByEmail.evictIfPresent(user.getEmail().toLowerCase());
                }
            }
        }

    }
    /*
    * check after 3 day
    * and check 0 second - 0 minutes - 1 AM - Every day - Every week
    * */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers(){
        userRepository.findAllByActivatedIsFalseAndActivationKeyNotNullAndCreatedDateBefore(
                Instant.now().minus(3, ChronoUnit.DAYS)
        ).forEach(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
        });
    }


}
