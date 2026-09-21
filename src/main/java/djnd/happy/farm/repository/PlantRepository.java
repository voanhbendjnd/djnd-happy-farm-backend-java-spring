package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long>, JpaSpecificationExecutor<Plant> {
    @Query(value = "select exists(select 1 from Plant p where lower(p.displayName) = :displayName)")
    boolean existsByDisplayNameIgnoreCase(@Param("displayName") String displayName);
    @Query(value = "select exists(select 1 from Plant p where lower(p.scientificName) = :scientficName)")
    boolean existsByScientificNameIgnoreCase(@Param("scientificName") String scientificName);

    @Query(value = "select exists(select 1 from Plant p where lower(p.displayName) = :displayName and p.id <> :id)")
    boolean existsByDisplayNameIgnoreCaseAndIdNot(@Param("displayName") String displayName, @Param("id") Long id);
    @Query(value = "select exists(select 1 from Plant p where lower(p.scientificName) = :scientificName and p.id <> :id)")
    boolean existsByScientificNameIgnoreCaseAndIdNot(@Param("scientificName") String scientificName, @Param("id") Long id);
    @Query(value = "select p from Plant p where lower(p.displayName) like concat('%',:q,'%') or lower(p.scientificName) like concat('%',:q,'%')",
    countQuery = "select count(p) from Plant p where lower(p.displayName) like concat('%',:q,'%') or lower(p.scientificName) like concat('%',:q,'%')")
    Page<Plant> fetchAllWithQuery(@Param("q") String q, Pageable pageable);
}
