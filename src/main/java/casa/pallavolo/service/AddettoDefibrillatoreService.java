package casa.pallavolo.service;

import casa.pallavolo.dto.AddettoDefibrillatoreDTO;
import casa.pallavolo.model.AddettoDefibrillatore;
import casa.pallavolo.repository.AddettoDefibrillatoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AddettoDefibrillatoreService {

    @Autowired
    private AddettoDefibrillatoreRepository addettoDefibrillatoreRepository;

    @Autowired
    private ModelMapper addettoMapper;

    public AddettoDefibrillatore getAddettoDefibrillatoreById(Integer id) throws Exception {
        return addettoDefibrillatoreRepository.findById(id)
                .orElseThrow(() -> new Exception("Addetto con ID " + id + " non trovato."));
    }

    public List<AddettoDefibrillatoreDTO> getAllAddetti() {
        return addettoDefibrillatoreRepository.findAll()
                .stream()
                .map(addetto -> addettoMapper.map(addetto, AddettoDefibrillatoreDTO.class))
                .sorted(Comparator.comparing(AddettoDefibrillatoreDTO::getCognome))
                .toList();
    }

    public AddettoDefibrillatoreDTO saveAddetto(AddettoDefibrillatoreDTO addettoDTO) throws Exception {
        if (Objects.isNull(addettoDTO.getNome()) || addettoDTO.getNome().isEmpty()) {
            throw new Exception("Il nome dell'addetto è obbligatorio.");
        }
        if (Objects.isNull(addettoDTO.getCognome()) || addettoDTO.getCognome().isEmpty()) {
            throw new Exception("Il cognome dell'addetto è obbligatorio.");
        }

        AddettoDefibrillatore addetto = addettoMapper.map(addettoDTO, AddettoDefibrillatore.class);
        AddettoDefibrillatore savedAddetto = addettoDefibrillatoreRepository.save(addetto);
        return addettoMapper.map(savedAddetto, AddettoDefibrillatoreDTO.class);
    }

    public AddettoDefibrillatoreDTO updateAddetto(AddettoDefibrillatoreDTO addettoDTO) throws Exception {
        if (Objects.isNull(addettoDTO.getId())) {
            throw new Exception("L'ID dell'addetto è obbligatorio per l'aggiornamento.");
        }

        Optional<AddettoDefibrillatore> existingAddettoOpt = addettoDefibrillatoreRepository.findById(addettoDTO.getId());
        if (existingAddettoOpt.isEmpty()) {
            throw new Exception("Addetto con ID " + addettoDTO.getId() + " non trovato.");
        }

        AddettoDefibrillatore existingAddetto = existingAddettoOpt.get();
        existingAddetto.setNome(addettoDTO.getNome());
        existingAddetto.setCognome(addettoDTO.getCognome());
        existingAddetto.setCodiceFiscale(addettoDTO.getCodiceFiscale());
        existingAddetto.setDataNascita(addettoDTO.getDataNascita());
        existingAddetto.setLuogoNascita(addettoDTO.getLuogoNascita());
        existingAddetto.setLuogoResidenza(addettoDTO.getLuogoResidenza());

        AddettoDefibrillatore updatedAddetto = addettoDefibrillatoreRepository.save(existingAddetto);
        return addettoMapper.map(updatedAddetto, AddettoDefibrillatoreDTO.class);
    }

    public void deleteAddettoById(Integer id) throws Exception {
        if (!addettoDefibrillatoreRepository.existsById(id)) {
            throw new Exception("Addetto con ID " + id + " non trovato.");
        }
        addettoDefibrillatoreRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return addettoDefibrillatoreRepository.existsById(id);
    }
}
