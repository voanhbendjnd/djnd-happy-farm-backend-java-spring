package djnd.happy.farm.security;

import djnd.happy.farm.domain.User;
import djnd.happy.farm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/*
* use component with define name it supportive for spring security 'AuthenticationManager' easy to find bean with name 'userDetailsService' at security where
* */
@Component("userDetailsService")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {
    final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        String lowerCaseUsername = username.toLowerCase(Locale.ENGLISH);
        if(new EmailValidator().isValid(username, null)){
            return userRepository.findOneWithAuthoritiesByEmail(lowerCaseUsername)
                    .map(existingUser -> this.createCustomUserDetails(lowerCaseUsername,existingUser))
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + lowerCaseUsername + " was not found!"));

        }
        return userRepository.findOneWithAuthoritiesByLogin(lowerCaseUsername)
                .map(existingUser -> this.createCustomUserDetails(lowerCaseUsername, existingUser))
                .orElseThrow(()-> new UsernameNotFoundException("User with login name " + lowerCaseUsername+" was not found!"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String username, User user) {
        if(!user.getActivated()){
            throw new UserNotActivatedException("User " + username + " was not activated!");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);

    }

    private CustomUserDetails createCustomUserDetails(String username, User user){
        if(!user.getActivated()){
            throw new UserNotActivatedException("User " + username + " was not activated!");
        }
        return new CustomUserDetails(user);
    }
}
