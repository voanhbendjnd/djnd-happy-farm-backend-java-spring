package djnd.happy.farm.repository;

import djnd.happy.farm.domain.PestSymptom;
import djnd.happy.farm.service.dto.PestSymptomDTO;
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
}
