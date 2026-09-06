package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PestDiseaseDTO {
    Long id;
    @NotNull(message = "Pest ID not found")
    Long pestId;
    @NotNull(message = "Disease ID not found")
    Long diseaseId;
    String description;
    String transmissionRole;
    String diseaseName;
    String diseaseSeverity;

}
