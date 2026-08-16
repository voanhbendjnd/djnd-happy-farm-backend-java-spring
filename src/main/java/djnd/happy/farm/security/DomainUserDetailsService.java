package djnd.happy.farm.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
/*
* use component with define name it supportive for spring security 'AuthenticationManager' easy to find bean with name 'userDetailsService' at security where
* */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
