package djnd.happy.farm.rest;

import djnd.happy.farm.domain.User;
import djnd.happy.farm.rest.vm.ManagedUserVM;
import djnd.happy.farm.service.InvalidPasswordException;
import djnd.happy.farm.service.UserService;
import djnd.happy.farm.service.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountResource {
    final UserService userService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
    if(checkPasswordLength(managedUserVM.getPassword())) {
        throw new InvalidPasswordException();
    }
    userService.registerUser(managedUserVM, managedUserVM.getPassword());
    }


    private static boolean checkPasswordLength(String password){
        return (
                !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH
                );
    }
}
