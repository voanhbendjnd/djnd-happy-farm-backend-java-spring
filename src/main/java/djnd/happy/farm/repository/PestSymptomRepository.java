package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PestSymptom;
import djnd.happy.farm.service.dto.PestSymptomDTO;
import djnd.happy.farm.service.projection.PestSymptomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PestSymptomRepository extends JpaRepository<PestSymptom, Long> {
    @Query(value = "select exists(select 1 from PestSymptom ps where lower(ps.name) = :name)")
    boolean existByNameIgnoreCaseDB(@Param("name") String name);

    @Query(value = "select exists(select 1 from PestSymptom ps where lower(ps.name) = :name and ps.id <> :id)")
    boolean existByNameIgnoreCaseDBAndIdNot(@Param("name") String name, @Param("id") Long id);

    long countByIdIn(List<Long> ids);

    @Query(value = """
        select ps from PestSymptom ps where lower(ps.name) like concat('%',:q,'%')
""",countQuery = "select count(ps) from PestSymptom ps where lower(ps.name) like concat('%',:q,'%')")
    Page<PestSymptom> fetchAllWithQueryAndPagination(@Param("q") String q, Pageable pageable);

    @Query(value = """
    SELECT ps.id AS id,
           ps.name AS name,
           psi.pest_id AS pestId
    FROM pest_symptom ps
    JOIN pest_symptom_intermediary psi
        ON ps.id = psi.pest_symptom_id
    WHERE psi.pest_id IN (:pestIds)
    """, nativeQuery = true)
    List<PestSymptomProjection> fetchWithPestIds(
            @Param("pestIds") List<Long> pestIds
    );

}
