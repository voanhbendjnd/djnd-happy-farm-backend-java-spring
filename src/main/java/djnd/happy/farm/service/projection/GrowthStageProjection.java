package djnd.happy.farm.service.projection;


import djnd.happy.farm.domain.GrowthStage;

public interface GrowthStageProjection {
    Long getFertilizerId();
    GrowthStage getGrowthStage();
}
