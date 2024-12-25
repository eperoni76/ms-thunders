package casa.pallavolo.controller;

import java.util.List;
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
	
	@GetMapping(Paths.GET_GIOCATORI)
	public ResponseEntity<List<GiocatoreDTO>> getAllGiocatori(){
		List<GiocatoreDTO> giocatori = giocatoreService.getAllGiocatori();
		if(giocatori.isEmpty()) {
			return noContentResult("Nessun risultato trovato");
		}
		return ResponseEntity.ok(giocatori);
	}
	
	@GetMapping(Paths.GET_GIOCATORE_BY_ID)
	public ResponseEntity<Giocatore> getGiocatoreById(@PathVariable Integer id){
		Giocatore giocatoreTrovato = giocatoreService.getGiocatoreById(id);
		if(giocatoreTrovato==null) {
			return noContentResult("Nessun giocatore trovato");
		}
		return ResponseEntity.ok(giocatoreTrovato);
	}
	
	@GetMapping(Paths.GET_GIOCATORI_BY_RUOLO)
	public ResponseEntity<List<GiocatoreDTO>> getGiocatoriByRuolo(@PathVariable String ruolo){
		List<GiocatoreDTO> giocatori = giocatoreService.getGiocatoriByRuolo(ruolo.toLowerCase());
		if(giocatori.isEmpty()) {
			return noContentResult("Nessun risultato trovato");
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
		 return noContentResult("Giocatore eliminato correttamente");
	}

	@PutMapping(Paths.UPDATE_GIOCATORE)
	public ResponseEntity<GiocatoreDTO> updateGiocatore(@RequestBody GiocatoreDTO giocatoreDaAggiornare){
		GiocatoreDTO giocatoreAggiornato = giocatoreService.aggiornaGiocatore(giocatoreDaAggiornare);
		return ResponseEntity.ok(giocatoreAggiornato);
	}
	
	private <T> ResponseEntity<T> noContentResult(String messaggio) {
		System.out.println(messaggio);
		return ResponseEntity.noContent().build();
	}
	
	
}