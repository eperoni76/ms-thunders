package casa.pallavolo.controller;

import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.service.SquadraService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(Paths.INSERT_SQUADRA)
    public ResponseEntity<SquadraDTO> insertSquadra(
            @RequestParam("nomeSquadra") String nomeSquadra,
            @RequestParam("nomeUfficiale") String nomeUfficiale,
            @RequestParam("campionato") String campionato,
            @RequestParam(value = "immagine", required = false) MultipartFile immagine) {

        SquadraDTO squadraDTO = new SquadraDTO();
        squadraDTO.setNomeSquadra(nomeSquadra);
        squadraDTO.setNomeUfficiale(nomeUfficiale);
        squadraDTO.setCampionato(campionato);

        SquadraDTO squadra = squadraMapper.map(squadraService.insertSquadra(squadraDTO, immagine), SquadraDTO.class);
        return squadra == null ? GenericUtils.noContentResult() : ResponseEntity.ok(squadra);
    }

    @GetMapping(Paths.GET_IMMAGINE_SQUADRA)
    public ResponseEntity<ByteArrayResource> getImmagineSquadra(@PathVariable String nomeSquadra) {
        ByteArrayResource immagine = squadraService.getImmagineSquadra(nomeSquadra);
        if (immagine == null) {
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(immagine);
    }

}
