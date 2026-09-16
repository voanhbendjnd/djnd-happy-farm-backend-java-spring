package djnd.happy.farm.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantImageDTO {
    String fileName;
    Boolean isPrimary;
}
