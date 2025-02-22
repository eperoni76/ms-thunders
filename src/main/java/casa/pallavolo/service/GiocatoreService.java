package casa.pallavolo.service;

import java.util.Comparator;
import java.util.List;

import casa.pallavolo.model.Squadra;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import casa.pallavolo.dto.GiocatoreDTO;
import casa.pallavolo.model.Giocatore;
import casa.pallavolo.repository.GiocatoreRepository;


@Service
public class GiocatoreService {
	
	@Autowired
	private GiocatoreRepository giocatoreRepository;
	@Autowired
	private ModelMapper giocatoreMapper;

	public List<GiocatoreDTO> getAllGiocatori(){
		return giocatoreRepository
				.findAll()
				.stream()
				.map(giocatore -> giocatoreMapper.map(giocatore, GiocatoreDTO.class))
				.sorted(Comparator.comparing(GiocatoreDTO::getCognome))
				.toList();
	}

	public List<GiocatoreDTO> getGiocatoriBySquadra(Squadra squadra){
		return giocatoreRepository
				.findBySquadra(squadra)
				.stream()
				.map(giocatore -> giocatoreMapper.map(giocatore, GiocatoreDTO.class))
				.sorted(Comparator.comparing(GiocatoreDTO::getCognome))
				.toList();
	}
	
	public GiocatoreDTO getGiocatoreById(Integer id) {
		Giocatore entity = giocatoreRepository.findById(id).orElse(null);
		return giocatoreMapper.map(entity, GiocatoreDTO.class);
	}
	
	public List<GiocatoreDTO> getGiocatoriByRuolo(String ruolo){
		return giocatoreRepository
				.findByRuolo(ruolo)
				.stream()
				.map(giocatore -> giocatoreMapper.map(giocatore, GiocatoreDTO.class))
				.sorted(Comparator.comparing(GiocatoreDTO::getCognome))
				.toList();
	}
	
	public Giocatore inserisciGiocatore(GiocatoreDTO giocatoreDaInserire) {
		if(giocatoreRepository.existsByNumeroMaglia(giocatoreDaInserire.getNumeroMaglia())){
			throw new IllegalArgumentException("Numero maglia già occupato");
		}
		return giocatoreRepository.save(giocatoreMapper.map(giocatoreDaInserire, Giocatore.class));
	}
	
	public void cancellaGiocatoreById(Integer id) {
		giocatoreRepository.deleteById(id);
	}

	public GiocatoreDTO aggiornaGiocatore(GiocatoreDTO giocatoreDaAggiornare){
		Giocatore entity = giocatoreRepository.findById(giocatoreDaAggiornare.getId()).orElseThrow(() -> new EntityNotFoundException("Giocatore non presente"));
		entity.setId(giocatoreDaAggiornare.getId());
		entity.setNome(giocatoreDaAggiornare.getNome());
		entity.setCognome(giocatoreDaAggiornare.getCognome());
		entity.setNumeroMaglia(giocatoreDaAggiornare.getNumeroMaglia());
		entity.setRuolo(giocatoreDaAggiornare.getRuolo());
		entity.setSquadra(giocatoreDaAggiornare.getSquadra());
		entity.setSquadra(giocatoreDaAggiornare.getSquadra());
		entity.setDataNascita(giocatoreDaAggiornare.getDataNascita());
		entity.setTesseraUisp(giocatoreDaAggiornare.getTesseraUisp());

		Giocatore giocatoreAggiornato = giocatoreRepository.save(entity);
		return giocatoreMapper.map(giocatoreAggiornato, GiocatoreDTO.class);
	}

	public GiocatoreDTO cambiaCapitano(GiocatoreDTO capitanoDaNominare){
		Giocatore capitanoAttuale = giocatoreRepository.findByIsCapitanoAndSquadra(true, capitanoDaNominare.getSquadra());
		capitanoAttuale.setIsCapitano(false);
		Giocatore nuovoCapitano = giocatoreRepository.findById(capitanoDaNominare.getId()).orElseThrow(() -> new EntityNotFoundException("Giocatore non presente"));
		nuovoCapitano.setIsCapitano(true);
		giocatoreRepository.save(capitanoAttuale);
		Giocatore capitanoAggiornato = giocatoreRepository.save(nuovoCapitano);
		return giocatoreMapper.map(capitanoAggiornato, GiocatoreDTO.class);
	}

	
	
	
}
