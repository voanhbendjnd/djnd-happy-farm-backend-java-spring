package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Habitat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface HabitatRepository extends JpaRepository<Habitat, String> {
    @Query(value = "select exists(select 1 from Habitat h where lower(h.name) = :name)")
    boolean existByName(@Param("name") String name);

    @Query(value = "select h from Habitat h where lower(h.name) like concat('%',:q,'%')", countQuery = "select count(h) from Habitat h where lower(h.name) like concat('%',:q,'%')")
    Page<Habitat> fetchAll(@Param("q") String name, Pageable pageable);


    Optional<Habitat> findByNameIgnoreCase(String name);

}
