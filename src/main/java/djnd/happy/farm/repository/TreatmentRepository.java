package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Treatment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Query(value = "select exists(select 1 from Treatment t where lower(t.method) = :lowerCaseMethod)")
    boolean checkByMethodIgnoreCase(@Param("lowerCaseMethod") String lowerCaseMethod);
    @Query(value = "select exists(select 1 from Treatment t where lower(t.method) = :lowerCaseMethod and t.id <> :id)")
    boolean checkByMethodIgnoreCaseAndIdNot(@Param("lowerCaseMethod") String method, @Param("id") Long id);


    @Query(value = """
        select t from Treatment t
        where lower(t.method) like concat('%',:lowerCaseMethod,'%')
""",countQuery = """
            select count(t) from Treatment t
        where lower(t.method) like concat('%',:lowerCaseMethod,'%')
        """
    )
    Page<Treatment> fetchAllWithQuery(@Param("lowerCaseMethod") String lowerCaseMethod, Pageable pageable);
}
