package casa.pallavolo.service;

import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.repository.SquadraRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SquadraService {
    @Autowired
    private SquadraRepository squadraRepository;
    @Autowired
    private ModelMapper squadraMapper;

    public List<SquadraDTO> getAllSquadre(){
        return squadraRepository
                .findAll()
                .stream()
                .map(squadra -> squadraMapper.map(squadra, SquadraDTO.class))
                .toList();
    }

    public SquadraDTO getSquadraById(Integer id){
        return squadraRepository.findById(id).map(squadra -> squadraMapper.map(squadra, SquadraDTO.class)).orElse(null);
    }
}
