package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentDTO {
    Long id;
    @NotBlank(message = "Treatment method name not found")
    String method;
    String description;
}
