package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiseaseDTO {
    Long id;
    @NotBlank(message = "Disease name cannot be empty")
    String name;
    String description;
    String severity;
}
