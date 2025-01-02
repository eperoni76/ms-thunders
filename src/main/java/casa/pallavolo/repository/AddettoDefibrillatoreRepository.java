package casa.pallavolo.repository;

import casa.pallavolo.model.AddettoDefibrillatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddettoDefibrillatoreRepository extends JpaRepository<AddettoDefibrillatore, Integer> {
}
