package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Taxonomy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, Long>{
    @Query(value = "select exists(select 1 from Taxonomy t where t.kingdom = :kingdom and t.family = :family and t.genus = :genus and t.species = :species)")
    boolean existByKingdomAndFamilyAndGenusAndSpecies(@Param("kingdom") String kingdom,
                                                      @Param("family") String family,
                                                        @Param("genus") String genus,
                                                      @Param("species") String species
    );

    @Query(value = """
        select t from Taxonomy t where lower(t.species) like concat('%',:q ,'%')
            and lower(t.family) like concat('%',:family,'%')
                and lower(t.genus) like concat('%',:genus,'%')
    """, countQuery = """
               select count(t) from Taxonomy t where lower(t.species) like concat('%',:q ,'%')
               and lower(t.family) like concat('%',:family,'%')
                            and lower(t.genus) like concat('%',:genus,'%')
    """)
    Page<Taxonomy> fetchAll(@Param("q") String q, @Param("family") String family, @Param("genus") String genus,  Pageable pageable);
}
