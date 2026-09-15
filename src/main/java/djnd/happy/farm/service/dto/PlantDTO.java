package djnd.happy.farm.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlantDTO {
    Long id;
    String displayName;
    String description;
    JsonNode descriptionJson;
    String scientificName;
    Boolean isCommunity;
    String status;
}
