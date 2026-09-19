package djnd.happy.farm.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Plant with display name not found")
    String displayName;
    String description;
    JsonNode descriptionJson;
    String scientificName;
    @NotBlank(message = "This plant cannot define community or not")
    Boolean isCommunity;
    @NotBlank(message = "Plant status not found")
    String status;
    List<PlantImageDTO> images;
}
