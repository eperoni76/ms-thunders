package casa.pallavolo.controller;

import java.util.List;

import casa.pallavolo.model.Squadra;
import casa.pallavolo.service.SquadraService;
import casa.pallavolo.utils.GenericUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import casa.pallavolo.dto.GiocatoreDTO;
import casa.pallavolo.model.Giocatore;
import casa.pallavolo.service.GiocatoreService;
import casa.pallavolo.utils.Paths;

@Controller
public class GiocatoreController {
	
	@Autowired
	private GiocatoreService giocatoreService;
	@Autowired
	private SquadraService squadraService;
	
	@GetMapping(Paths.GET_GIOCATORI)
	public ResponseEntity<List<GiocatoreDTO>> getAllGiocatori(){
		List<GiocatoreDTO> giocatori = giocatoreService.getAllGiocatori();
		if(giocatori.isEmpty()) {
			return GenericUtils.noContentResult();
		}
		return ResponseEntity.ok(giocatori);
	}
	
	@GetMapping(Paths.GET_GIOCATORE_BY_ID)
	public ResponseEntity<GiocatoreDTO> getGiocatoreById(@PathVariable Integer id){
		GiocatoreDTO giocatoreTrovato = giocatoreService.getGiocatoreById(id);
		if(giocatoreTrovato==null) {
			return GenericUtils.noContentResult();
		}
		return ResponseEntity.ok(giocatoreTrovato);
	}
	
	@GetMapping(Paths.GET_GIOCATORI_BY_RUOLO)
	public ResponseEntity<List<GiocatoreDTO>> getGiocatoriByRuolo(@PathVariable String ruolo){
		List<GiocatoreDTO> giocatori = giocatoreService.getGiocatoriByRuolo(ruolo.toLowerCase());
		if(giocatori.isEmpty()) {
			return GenericUtils.noContentResult();
		}
		return ResponseEntity.ok(giocatori);
	}

	@GetMapping(Paths.GET_GIOCATORI_BY_SQUADRA)
	public ResponseEntity<List<GiocatoreDTO>> getGiocatoriBySquadra(@PathVariable Integer idSquadra){
		Squadra squadra = squadraService.getSquadraById(idSquadra);
		List<GiocatoreDTO> giocatori = giocatoreService.getGiocatoriBySquadra(squadra);
		if(giocatori.isEmpty()) {
			return GenericUtils.noContentResult();
		}
		return ResponseEntity.ok(giocatori);
	}

	@PostMapping(Paths.INSERT_GIOCATORE)
	public ResponseEntity<?> inserisciGiocatore(@RequestBody GiocatoreDTO giocatoreDaInserire){
		try{
			Giocatore giocatoreInserito = giocatoreService.inserisciGiocatore(giocatoreDaInserire);
			return ResponseEntity.ok(giocatoreInserito);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping(Paths.DELETE_GIOCATORE_BY_ID)
	public ResponseEntity<Void> cancellaGiocatoreById(@PathVariable Integer id){
		 giocatoreService.cancellaGiocatoreById(id);
		 return GenericUtils.noContentResult();
	}

	@PutMapping(Paths.UPDATE_GIOCATORE)
	public ResponseEntity<GiocatoreDTO> updateGiocatore(@RequestBody GiocatoreDTO giocatoreDaAggiornare){
		GiocatoreDTO giocatoreAggiornato = giocatoreService.aggiornaGiocatore(giocatoreDaAggiornare);
		return ResponseEntity.ok(giocatoreAggiornato);
	}

	@PutMapping(Paths.CAMBIA_CAPITANO)
	public ResponseEntity<GiocatoreDTO> cambiaCapitano(@RequestBody GiocatoreDTO capitanoDaNominare){
		GiocatoreDTO nuovoCapitano = giocatoreService.cambiaCapitano(capitanoDaNominare);
		return ResponseEntity.ok(nuovoCapitano);
	}
	
	
}
