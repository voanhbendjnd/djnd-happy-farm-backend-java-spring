package djnd.happy.farm.config;

import djnd.happy.farm.domain.Authority;
import djnd.happy.farm.domain.GrowthStage;
import djnd.happy.farm.domain.User;
import djnd.happy.farm.domain.enums.LoginType;
import djnd.happy.farm.repository.AuthorityRepository;
import djnd.happy.farm.repository.GrowthStageRepository;
import djnd.happy.farm.repository.UserRepository;
import djnd.happy.farm.security.AuthoritiesConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
    final UserRepository userRepository;
    final AuthorityRepository authorityRepository;
    final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        log.info("Database start check initialization...");
        Long totalUsers = userRepository.count();
        Long totalAuthority = authorityRepository.count();
        Set<Authority> authorities = new HashSet<>();
        if(totalAuthority.equals(0L)) {
            log.info("Start create authority...");

            Authority adminAuthority = new Authority();
            adminAuthority.setName(AuthoritiesConstants.ADMIN);
            Authority userAuthority = new Authority();
            userAuthority.setName(AuthoritiesConstants.USER);
            Authority anonymousAuthority = new Authority();
            anonymousAuthority.setName(AuthoritiesConstants.ANONYMOUS);
            authorities.addAll(List.of(adminAuthority, userAuthority, anonymousAuthority));
            authorityRepository.saveAll(authorities);
        }
        if(totalUsers.equals(0L)){
            log.info("Start create user...");
            User admin = new User();
            admin.setLogin("admin");
            admin.setName("VO ANH BEN");
            admin.setActivated(true);
            admin.setEmail("benva.ce190709@gmail.com");
            admin.setLangKey(Constants.DEFAULT_LANGUAGE);
            admin.setPassword(passwordEncoder.encode("123123"));
            admin.setLoginType(LoginType.SYSTEM.toString());
            admin.setAuthorities(authorities);
            userRepository.save(admin);
        }
        if(totalUsers > 0 || totalAuthority > 0){
            log.info("Skip processing initialize...");
        }
        else{
            log.info("End init data and init data success");
        }
    }
}
