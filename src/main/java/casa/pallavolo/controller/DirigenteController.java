package casa.pallavolo.controller;

import casa.pallavolo.dto.DirigenteDTO;
import casa.pallavolo.service.DirigenteService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping(Paths.DIRIGENTI_BASE)
public class DirigenteController {

    @Autowired
    private DirigenteService dirigenteService;
    @Autowired
    private ModelMapper dirigenteMapper;

    @GetMapping(Paths.GET_ALL_DIRIGENTI)
    public ResponseEntity<List<DirigenteDTO>> getAllDirigenti(){
        List<DirigenteDTO> dirigenti = dirigenteService.getAllDirigenti();
        if(dirigenti.isEmpty()){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(dirigenti);
    }

    @GetMapping(Paths.GET_ALL_ALLENATORI)
    public ResponseEntity<List<DirigenteDTO>> getAllAllenatori(){
        List<DirigenteDTO> allenatori = dirigenteService.getAllAllenatori();
        if(allenatori.isEmpty()){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(allenatori);
    }

    @PostMapping(Paths.INSERT_DIRIGENTE)
    public ResponseEntity<DirigenteDTO> inserisciDirigente(@RequestBody DirigenteDTO dirigenteDaInserire){
        DirigenteDTO dirigenteInserito = dirigenteMapper.map(dirigenteService.inserisciDirigente(dirigenteDaInserire), DirigenteDTO.class);
        if(Objects.isNull(dirigenteInserito)){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(dirigenteInserito);
    }

    @PutMapping(Paths.UPDATE_DIRIGENTE)
    public ResponseEntity<DirigenteDTO> modificaDirigente(@RequestBody DirigenteDTO dirigente){
        DirigenteDTO dirigenteAggiornato = dirigenteService.modificaDirigente(dirigente);
        if(Objects.isNull(dirigenteAggiornato)){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(dirigenteAggiornato);
    }

    @DeleteMapping(Paths.DELETE_DIRIGENTE_BY_ID)
    public ResponseEntity<Void> deleteDirigente(@PathVariable Integer id){
        dirigenteService.deleteDirigente(id);
        return ResponseEntity.ok().build();
    }
}
