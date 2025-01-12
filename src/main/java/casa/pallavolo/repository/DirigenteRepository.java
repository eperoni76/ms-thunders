package casa.pallavolo.repository;

import casa.pallavolo.model.Dirigente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirigenteRepository extends JpaRepository<Dirigente, Integer> {
    List<Dirigente> findByAllenatoreTrue();
}
