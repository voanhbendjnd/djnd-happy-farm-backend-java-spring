package djnd.happy.farm.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;


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
    List<PlantImageDTO> images;
}
