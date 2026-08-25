package djnd.happy.farm.repository;

import djnd.happy.farm.domain.GrowthStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowthStageRepository extends JpaRepository<GrowthStage, Long> {
    long countByIdIn(List<Long> ids);

}
