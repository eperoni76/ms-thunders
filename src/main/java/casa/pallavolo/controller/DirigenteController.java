package casa.pallavolo.controller;

import casa.pallavolo.dto.DirigenteDTO;
import casa.pallavolo.service.DirigenteService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(Paths.DIRIGENTI_BASE)
public class DirigenteController {

    @Autowired
    private DirigenteService dirigenteService;

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
}
