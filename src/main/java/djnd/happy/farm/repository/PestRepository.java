package djnd.happy.farm.repository;

import djnd.happy.farm.domain.Pest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PestRepository extends JpaRepository<Pest, Long> {
}
