package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PlantPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantPartRepository extends JpaRepository<PlantPart, Long> {
    @Query(value = "select exists(select 1 from PlantPart pp where lower(pp.name) = :name)")
    boolean checkByName(@Param("name") String name);
    @Query(value = "select exists(select 1 from PlantPart pp where lower(pp.name) = :name and pp.id <> :id)")
    boolean checkByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query(value = "select pp from PlantPart pp where lower(pp.name) like concat('%',:queryName,'%')", countQuery = "select count(pp) from PlantPart pp where lower(pp.name) like concat('%',:queryName,'%')")
    Page<PlantPart> fetchAllByName(@Param("queryName") String name, Pageable pageable);
}
