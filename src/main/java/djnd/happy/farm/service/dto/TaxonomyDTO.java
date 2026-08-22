package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaxonomyDTO {
    Long id;
    String kingdom;
    @NotBlank(message = "Family taxonomy not found!")
    String family;
    String genus;
    String species;
}
