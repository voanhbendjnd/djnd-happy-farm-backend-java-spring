package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Fertilizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer,Long> , JpaSpecificationExecutor<Fertilizer> {
    boolean existsByNameIgnoreCase(String name);
    @Query(value = """
SELECT DISTINCT f.* FROM fertilizers f
LEFT JOIN fertilizer_growth_stage fgs ON f.id = fgs.fertilizer_id
                    left join growth_stages gs on gs.id = fgs.growth_stage_id
WHERE 
    (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:type IS NULL OR LOWER(f.type) = LOWER(:type))
    AND ((:minNitro IS NULL AND :maxNitro IS NULL) OR f.nitrogen BETWEEN :minNitro AND :maxNitro)
    AND ((:minPho IS NULL AND :maxPho IS NULL) OR f.phosphorus BETWEEN :minPho AND :maxPho)
    AND ((:minPo IS NULL AND :maxPo IS NULL) OR f.potassium BETWEEN :minPo AND :maxPo)
    AND (:growthStageId IS NULL OR fgs.growth_stage_id = :growthStageId)
""",
            countQuery = """
SELECT COUNT(DISTINCT f.id) FROM fertilizers f
LEFT JOIN fertilizer_growth_stage fgs ON f.id = fgs.fertilizer_id
                    left join growth_stages gs on gs.id = fgs.growth_stage_id
WHERE 
    (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')))
    AND (:type IS NULL OR LOWER(f.type) = LOWER(:type))
    AND ((:minNitro IS NULL AND :maxNitro IS NULL) OR f.nitrogen BETWEEN :minNitro AND :maxNitro)
    AND ((:minPho IS NULL AND :maxPho IS NULL) OR f.phosphorus BETWEEN :minPho AND :maxPho)
    AND ((:minPo IS NULL AND :maxPo IS NULL) OR f.potassium BETWEEN :minPo AND :maxPo)
    AND (:growthStageId IS NULL OR fgs.growth_stage_id = :growthStageId)
""",
            nativeQuery = true)
    Page<Fertilizer> fetchAll(
            @Param("name") String name,
            @Param("type") String fertilizerType,
            @Param("minNitro") BigDecimal minNitro,
            @Param("maxNitro") BigDecimal maxNitro,
            @Param("minPho") BigDecimal minPho,
            @Param("maxPho") BigDecimal maxPho,
            @Param("minPo") BigDecimal minPo,
            @Param("maxPo") BigDecimal maxPo,
            @Param("growthStageId") Long growthStageId,
            Pageable pageable
    );
}
