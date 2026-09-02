package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Pest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PestRepository extends JpaRepository<Pest, Long> {
    @Query(value = "select exists(select 1 from Pest p where lower(p.name) = :name)")
    boolean checkByName(@Param("name") String name);

    @Query(value = "select exists(select 1 from Pest p where lower(p.name) = :name and p.id <> :id)")
    boolean checkByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query(value = "select p from Pest p where lower(p.name) like concat('%',:name,'%')", countQuery = "select count(p) from Pest p where lower(p.name) like concat('%',:name,'%')")
    Page<Pest> fetchAllWithQuery(@Param("name") String name, Pageable pageable);
}
