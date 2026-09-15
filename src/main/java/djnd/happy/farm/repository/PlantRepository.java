package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long>, JpaSpecificationExecutor<Plant> {
    @Query(value = "select exists(select 1 from Plant p where lower(p.displayName) = :displayName)")
    boolean existsByNameIgnoreCase(@Param("displayName") String displayName);


    @Query(value = "select exists(select 1 from Plant p where lower(p.displayName) = :displayName and p.id <> :id)")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("displayName") String displayName, @Param("id") Long id);
}
