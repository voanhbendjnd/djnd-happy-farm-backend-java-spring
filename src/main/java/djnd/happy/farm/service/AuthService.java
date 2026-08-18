package djnd.happy.farm.service;

import djnd.happy.farm.domain.Authority;
import djnd.happy.farm.domain.User;
import djnd.happy.farm.service.dto.ResLoginDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class AuthService {
    public ResLoginDTO generateResLoginDTO(User user) {
        ResLoginDTO res = new ResLoginDTO();
        var userLogin = new ResLoginDTO.UserLogin();
        userLogin.setLogin(user.getLogin());
        if(user.getEmail() != null &&  !user.getEmail().isEmpty()) {
            userLogin.setEmail(user.getEmail());
        }
        userLogin.setId(user.getId());
        userLogin.setLoginType(user.getLoginType());
        userLogin.setName(user.getName());
        userLogin.setAuthorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }
}
