package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantPartDTO {
    Long id;
    @NotNull(message = "name plant part not found")
    String name;
    String description;
}
