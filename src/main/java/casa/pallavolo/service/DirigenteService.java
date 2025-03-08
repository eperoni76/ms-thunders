package casa.pallavolo.service;

import casa.pallavolo.dto.DirigenteDTO;
import casa.pallavolo.model.Dirigente;
import casa.pallavolo.repository.DirigenteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DirigenteService {
    @Autowired
    private DirigenteRepository dirigenteRepository;
    @Autowired
    private ModelMapper dirigenteMapper;

    public List<DirigenteDTO> getAllDirigenti(){
        return dirigenteRepository.findAll()
                .stream()
                .map(dirigente -> dirigenteMapper.map(dirigente, DirigenteDTO.class))
                .sorted((d1, d2) -> d1.getCognome().compareTo(d2.getCognome()))
                .toList();

    }

    public List<DirigenteDTO> getAllAllenatori() {
        return dirigenteRepository.findByAllenatoreTrue()
                .stream()
                .map(dir -> dirigenteMapper.map(dir, DirigenteDTO.class))
                .toList();
    }

    public Dirigente getDirigenteById(Integer id){
        return dirigenteRepository.findById(id).orElse(null);
    }

    public Dirigente inserisciDirigente(DirigenteDTO dirigenteDaInserire){
        Dirigente dirigente = dirigenteMapper.map(dirigenteDaInserire, Dirigente.class);
        return dirigenteRepository.save(dirigente);
    }

    public DirigenteDTO modificaDirigente(DirigenteDTO dirigente) {
        Dirigente dirigenteAggiornato = dirigenteRepository.findById(dirigente.getId()).orElse(null);
        if (Objects.nonNull(dirigenteAggiornato)) {
            dirigenteAggiornato.setNome(dirigente.getNome());
            dirigenteAggiornato.setCognome(dirigente.getCognome());
            dirigenteAggiornato.setTesseraUisp(dirigente.getTesseraUisp());
            dirigenteAggiornato.setAllenatore(dirigente.getAllenatore());
            dirigenteRepository.save(dirigenteAggiornato);
            return dirigenteMapper.map(dirigenteAggiornato, DirigenteDTO.class);
        }
        return null;
    }

    public void deleteDirigente(Integer id) {
        dirigenteRepository.deleteById(id);
    }
}
