package djnd.happy.farm.service.dto;

import djnd.happy.farm.domain.GrowthStage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FertilizerGrowthStageDTO extends FertilizerDTO {
    List<Long> growthStageIds;
    Set<GrowthStage> growthStages;

}
