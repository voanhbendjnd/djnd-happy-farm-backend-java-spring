package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PlantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantImageRepository extends JpaRepository<PlantImage, Long> {
    @Query(value = "select pi from PlantImage pi where pi.plantId = :plantId")
    List<PlantImage> findByPlantId(@Param("plantId") Long plantId);
    @Query(value = "select pi from PlantImage pi where p.plantId in :plantIds")
    List<PlantImage> findByPlantIdIn(@Param("plantIds") List<Long> plantIds);
}
