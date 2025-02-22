package casa.pallavolo.repository;

import java.util.List;

import casa.pallavolo.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import casa.pallavolo.model.Giocatore;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Integer>{
	List<Giocatore> findByRuolo(String ruolo);
	boolean existsByNumeroMaglia(int numeroMaglia);
	List<Giocatore> findBySquadra(Squadra squadra);
	Giocatore findByIsCapitanoAndSquadra(boolean isCapitano, Squadra squadra);
}
