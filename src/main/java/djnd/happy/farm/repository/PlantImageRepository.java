package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PlantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantImageRepository extends JpaRepository<PlantImage, Long> {
}
