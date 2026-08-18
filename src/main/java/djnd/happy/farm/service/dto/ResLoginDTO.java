package djnd.happy.farm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    String refreshToken;
    UserLogin user;
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

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class UserInsideToken{
        Long id;
        String login;
    }

}
