package casa.pallavolo.controller;

import casa.pallavolo.dto.GaraDTO;
import casa.pallavolo.dto.GiocatoreDTO;
import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.model.Gara;
import casa.pallavolo.model.Giocatore;
import casa.pallavolo.service.GaraService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Paths.GARE_BASE)
public class GaraController {

    @Autowired
    private GaraService garaService;

    @GetMapping(Paths.GET_ALL_GARE)
    public ResponseEntity<List<GaraDTO>> getAllGare(){
        List<GaraDTO> gare = garaService.getAllGare();
        if(gare.isEmpty()){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(gare);
    }

    @PostMapping(Paths.INSERT_GARA)
    public ResponseEntity<?> inserisciGara(@RequestBody GaraDTO garaDaInserire){
        try{
            Gara garaInserita = garaService.inserisciGara(garaDaInserire);
            return ResponseEntity.ok(garaInserita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(Paths.UPDATE_GARA)
    public ResponseEntity<GaraDTO> updateGiocatore(@RequestBody GaraDTO garaDaModificare){
        GaraDTO giocatoreAggiornato = garaService.modificaGara(garaDaModificare);
        return ResponseEntity.ok(giocatoreAggiornato);
    }
}
