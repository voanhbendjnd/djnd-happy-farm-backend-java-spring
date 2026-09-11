package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Propagation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropagationRepository extends JpaRepository<Propagation, Long>, JpaSpecificationExecutor<Propagation> {
    @Query(value = "select exists(select 1 from Propagation p where lower(p.method) = :methodLowerCase)")
    boolean existsByMethodIgnoreCase(@Param("methodLowerCase") String methodLowerCase);
    @Query(value = "select exists(select 1 from Propagation p where lower(p.method) = :methodLowerCase and p.id <> :id)")
    boolean existsByMethodIgnoreCaseAndIdNot(@Param("methodLowerCase") String methodLowerCase, @Param("id") Long id);


    @Query(value = """
        select p from Propagation p where (p.method is null or p.method = :method) and (p.difficulty is null or p.difficulty = :difficulty)
""", countQuery = "select count(p) from Propagation p where (p.method is null or p.method = :method) and (p.difficulty is null or p.difficulty = :difficulty)")
    Page<Propagation> filterByMethodAndDifficulty(@Param("method")String method,@Param("difficulty") String difficulty,Pageable pageable);

}

