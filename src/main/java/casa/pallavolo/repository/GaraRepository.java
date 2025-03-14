package casa.pallavolo.repository;

import casa.pallavolo.model.Gara;
import casa.pallavolo.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GaraRepository extends JpaRepository<Gara, Integer> {
    @Query("SELECT g FROM Gara g WHERE g.squadra.id = :idSquadra")
    List<Gara> findByIdSquadra(@Param("idSquadra") Integer idSquadra);

    @Query("SELECT COUNT(g) FROM Gara g WHERE g.squadra = :squadra AND g.isVittoria = true")
    Integer countVittorieBySquadra(@Param("squadra") Squadra squadra);

    @Query("SELECT COUNT(g) FROM Gara g WHERE g.squadra = :squadra AND g.isVittoria = false")
    Integer countSconfitteBySquadra(@Param("squadra") Squadra squadra);
}
