package djnd.happy.farm.service;

import djnd.happy.farm.domain.Authority;
import djnd.happy.farm.domain.User;
import djnd.happy.farm.repository.UserRepository;
import djnd.happy.farm.rest.errors.ResourceNotFoundException;
import djnd.happy.farm.security.SecurityUtils;
import djnd.happy.farm.service.dto.ResLoginDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class AuthService {
    final SecurityUtils securityUtils;
    final SessionManager sessionManager;
    final UserRepository userRepository;
    @Transactional
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
        Set<String> authorities =user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        userLogin.setAuthorities(authorities);
        String sessionId = sessionManager.initSessionId(user.getId());
        String newAccessToken = securityUtils.createAccessToken(userLogin, sessionId, authorities);
        res.setUser(userLogin);
        res.setAccessToken(newAccessToken);
        String newRefreshToken = securityUtils.createRefreshToken(userLogin);
        int updatedRefreshToken = userRepository.updateRefreshTokenById(user.getId(), newRefreshToken);
        if(updatedRefreshToken <= 0) {
            throw new ResourceNotFoundException("User ID not found");
        }
        res.setRefreshToken(newRefreshToken);
        return res;
    }
}
