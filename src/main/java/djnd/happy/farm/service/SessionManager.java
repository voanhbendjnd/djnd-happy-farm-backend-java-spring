package djnd.happy.farm.service;

import djnd.happy.farm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SessionManager {
    final UserRepository userRepository;

    public String initSessionId(Long userId){
        String newSessionId = UUID.randomUUID().toString();
        int updated = userRepository.updateSessionIdById(userId, newSessionId);
        if(updated > 0){
            return newSessionId;
        }
        throw new ResourceNotFoundException("Cannot init session ID!");
    }


    public boolean isValidSessionId(String loginName, String sessionId){
        return userRepository.findOneWithAuthoritiesByLogin(loginName).map(existingUser -> existingUser.getSessionId().equals(sessionId)).orElse(false);
    }
}
