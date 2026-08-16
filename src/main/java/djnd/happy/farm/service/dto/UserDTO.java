package djnd.happy.farm.service.dto;

import djnd.happy.farm.domain.Authority;
import djnd.happy.farm.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserDTO {

    Long id;
    String login;
    String name;
    String email;
    String langKey;
    String createdBy;
    Instant createdDate;
    String lastModifiedBy;
    Instant lastModifiedDate;
    boolean activated;
    Set<String> authorities;
    public UserDTO(){}
    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
    }

}
