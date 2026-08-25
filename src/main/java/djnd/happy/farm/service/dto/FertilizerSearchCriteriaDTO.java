package djnd.happy.farm.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FertilizerSearchCriteriaDTO {
    String name; // name fertilizer
    String fertilizerType;
    String npkRatio;
    String description;
    BigDecimal minNitrogen;
    BigDecimal maxNitrogen;

    BigDecimal minPhosphorus;
    BigDecimal maxPhosphorus;

    BigDecimal minPotassium;
    BigDecimal maxPotassium;

    Long growthStageId;


}
