package casa.pallavolo.service;

import casa.pallavolo.dto.GaraDTO;
import casa.pallavolo.model.Gara;
import casa.pallavolo.model.Squadra;
import casa.pallavolo.repository.GaraRepository;
import casa.pallavolo.repository.SquadraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GaraService {
    @Autowired
    private GaraRepository garaRepository;
    @Autowired
    private SquadraRepository squadraRepository;
    @Autowired
    private ModelMapper garaMapper;

    public Gara inserisciGara(GaraDTO garaDaInserire){
        Gara gara = garaMapper.map(garaDaInserire, Gara.class);
        Optional<Squadra> squadra = squadraRepository.findById(garaDaInserire.getSquadra().getId());
        gara.setSquadra(squadra.orElse(null));
        return garaRepository.save(gara);
    }

    public List<GaraDTO> getAllGare(){
        return garaRepository
                .findAll()
                .stream()
                .map(squadra -> garaMapper.map(squadra, GaraDTO.class))
                .sorted(Comparator.comparing(GaraDTO::getData))
                .toList();
    }

    public GaraDTO modificaGara(GaraDTO garaDaModificare){
        Gara entity = garaRepository.findById(garaDaModificare.getId()).orElseThrow(() -> new EntityNotFoundException("Gara non presente"));
        Squadra squadra = squadraRepository.findById(garaDaModificare.getSquadra().getId()).orElse(null);
        entity.setId(garaDaModificare.getId());
        entity.setSquadra(squadra);
        entity.setNumeroGara(garaDaModificare.getNumeroGara());
        entity.setData(garaDaModificare.getData());
        entity.setOra(garaDaModificare.getOra());
        entity.setIndirizzo(garaDaModificare.getIndirizzo());
        entity.setCampionato(garaDaModificare.getCampionato());
        entity.setOspitante(garaDaModificare.getOspitante());
        entity.setOspite(garaDaModificare.getOspite());
        entity.setIsTrasferta(garaDaModificare.getIsTrasferta());

        Gara garaAggiornata = garaRepository.save(entity);
        return garaMapper.map(garaAggiornata, GaraDTO.class);
    }

    public void eliminaGaraById(Integer id){
        garaRepository.deleteById(id);
    }
}
