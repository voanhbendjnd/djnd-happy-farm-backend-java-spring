package djnd.happy.farm.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResLoginDTO {
    String accessToken;
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserLogin{
        Long id;
        String email;
        String name;
        Set<String> authorities;
        String login;
        String loginType;
    }

}
