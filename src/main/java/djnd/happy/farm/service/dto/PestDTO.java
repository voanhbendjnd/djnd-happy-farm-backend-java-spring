package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PestDTO {
    Long id;
    @NotBlank(message = "Pest name not found")
    String name;
    String description;
    Set<PestSymptomDTO> pestSymptoms = new HashSet<>();
}
