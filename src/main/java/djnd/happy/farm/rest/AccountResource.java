package djnd.happy.farm.rest;

import djnd.happy.farm.domain.User;
import djnd.happy.farm.rest.errors.InvalidPasswordException;
import djnd.happy.farm.rest.vm.LoginVM;
import djnd.happy.farm.rest.vm.ManagedUserVM;
import djnd.happy.farm.security.CustomUserDetails;
import djnd.happy.farm.security.SecurityUtils;
import djnd.happy.farm.service.AuthService;
import djnd.happy.farm.service.UserService;
import djnd.happy.farm.service.dto.ResLoginDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountResource {
    @Value("${djnd.jwt.refresh-token-validity-in-seconds}")
    private  Long refreshTokenExpiration;
    final UserService userService;
    final AuthService authService;
    final AuthenticationManagerBuilder authenticationManagerBuilder;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
    if(!checkPasswordLength(managedUserVM.getPassword())) {
        throw new InvalidPasswordException();
    }
    userService.registerUser(managedUserVM, managedUserVM.getPassword());
    }

    /*
    * DaoAuthenticationProvider check password with method passwordEncoder.matches(clientPassword, hash_password_from_DB)
    * */
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> loginWithCredentials(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername().toLowerCase(), loginVM.getPassword());
        try{
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(userToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user =userDetails.user();
            ResLoginDTO res = authService.generateResLoginDTO(user);
            ResponseCookie cookie = ResponseCookie.from("refresh_token",res.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .sameSite("Strict") // handle CSRF
                    .build();
            res.setRefreshToken(null);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(res);
        }
        catch(BadCredentialsException e){
            throw new djnd.happy.farm.rest.errors.BadCredentialsException();
        }
    }



    private static boolean checkPasswordLength(String password){
        return (
                !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH
                );
    }
}
