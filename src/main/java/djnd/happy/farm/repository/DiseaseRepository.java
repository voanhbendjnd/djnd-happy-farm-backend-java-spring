package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    @Query(value = "select exists(select 1 from Disease d where lower(d.name) = :normalizedName)")
    boolean checkByNameIgnoreCase(@Param("normalizedName") String name);
    @Query(value = "select exists(select 1 from Disease d where lower(d.name) = :normalizedName and d.id <> :id)")
    boolean checkByNameIgnoreCaseAndIdNot(@Param("normalizedName") String name, @Param("id") Long id);


    @Query(value ="select d from Disease d where lower(d.name) like concat('%',:name,'%') and d.severity = :severity",
    countQuery = "select count(d) from Disease d where lower(d.name) like concat('%',:name,'%') and d.severity = :severity")
    Page<Disease> fetchAllWithQuery(@Param("name") String name, @Param("severity") String severity, Pageable pageable);
}
