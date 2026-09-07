package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PestDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PestDiseaseRepository extends JpaRepository<PestDisease, Long> {
    @Query(value = "select exists(select 1 from PestDisease pd where pd.pestId = :pestId and pd.diseaseId = :diseaseId)")
    boolean existsByPestIdAndDiseaseId(@Param("pestId") Long pestId, @Param("diseaseId") Long diseaseId);

    @Query(value = "select exists(select 1 from PestDisease pd where pd.pestId = :pestId and pd.diseaseId = :diseaseId and pd.id <> :id)")
    boolean existsByPestIdAndDiseaseIdAndIdNot(@Param("pestId") Long pestId, @Param("diseaseId") Long diseaseId, @Param("id") Long id);
    @Query(value = "select pd from PestDisease pd where pd.pestId = :pestId")
    List<PestDisease> findAllByPestId(@Param("pestId")Long  pestId);
}
