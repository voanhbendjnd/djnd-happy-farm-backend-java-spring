package djnd.happy.farm.repository;

import djnd.happy.farm.domain.FertilizerGrowthStage;
import djnd.happy.farm.domain.GrowthStage;
import djnd.happy.farm.service.projection.GrowthStageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FertilizerGrowthStageRepository extends JpaRepository<FertilizerGrowthStage,Long> {


    void deleteByFertilizerId(Long fertilizerId);
    List<FertilizerGrowthStage> findByFertilizerIdIn(List<Long> fertilizerIds);

    @Query(value = "select fgs.fertilizerId as fertilizerId, gs as growthStage from FertilizerGrowthStage fgs join GrowthStage gs on fgs.growthStageId = gs.id  where fgs.fertilizerId in :fertilizerIds")
    List<GrowthStageProjection> findByGrowthStageIdIn(@Param("fertilizerIds") List<Long> fertilizerIds);
}
