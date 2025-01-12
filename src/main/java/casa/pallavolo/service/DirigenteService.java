package casa.pallavolo.service;

import casa.pallavolo.dto.DirigenteDTO;
import casa.pallavolo.model.Dirigente;
import casa.pallavolo.repository.DirigenteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
