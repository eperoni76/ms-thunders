package casa.pallavolo.repository;

import casa.pallavolo.model.Gara;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GaraRepository extends JpaRepository<Gara, Integer> {
}
