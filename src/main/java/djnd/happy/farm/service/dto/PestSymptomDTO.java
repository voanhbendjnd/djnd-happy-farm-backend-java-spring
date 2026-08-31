package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PestSymptomDTO {
    Long id;
    @NotBlank(message = "Cannot found name pest symptom")
    String name;
    String description;
}
