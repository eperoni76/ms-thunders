package casa.pallavolo.controller;

import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.service.SquadraService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping(Paths.SQUADRE_BASE)
public class SquadraController {

    @Autowired
    private SquadraService squadraService;
    @Autowired
    private ModelMapper squadraMapper;

    @GetMapping(Paths.GET_ALL_SQUADRE)
    public ResponseEntity<List<SquadraDTO>> getAllSquadre(){
        List<SquadraDTO> squadre = squadraService.getAllSquadre();
        if(squadre.isEmpty()){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(squadre);
    }

    @GetMapping(Paths.GET_SQUADRA_BY_ID)
    public ResponseEntity<SquadraDTO> getSquadraById(@PathVariable Integer idSquadra){
        SquadraDTO squadra = squadraMapper.map(squadraService.getSquadraById(idSquadra), SquadraDTO.class);
        return squadra == null ? GenericUtils.noContentResult() : ResponseEntity.ok(squadra);
    }
}
