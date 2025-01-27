package casa.pallavolo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import casa.pallavolo.model.Giocatore;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Integer>{
	List<Giocatore> findByRuolo(String ruolo);
	boolean existsByNumeroMaglia(int numeroMaglia);
	List<Giocatore> findBySquadra(Integer squadra);
}
