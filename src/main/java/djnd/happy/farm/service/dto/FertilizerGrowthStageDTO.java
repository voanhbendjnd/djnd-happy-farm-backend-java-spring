package djnd.happy.farm.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FertilizerGrowthStageDTO extends FertilizerDTO {
    List<Long> growthStageIds;

}
