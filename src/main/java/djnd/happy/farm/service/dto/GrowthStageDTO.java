package djnd.happy.farm.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrowthStageDTO {
    Long id;
    @NotNull
    String code;
    String name;
    String description;
}
