package djnd.happy.farm.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FertilizerDTO{
    Long id;
    @NotBlank
    String name;
    String type;
    String description;
    @Min(value = 0)
    BigDecimal nitrogen;
    @Min(value = 0)
    BigDecimal phosphorus;
    @Min(value = 0)
    BigDecimal potassium;
    JsonNode descriptionJson;

}
