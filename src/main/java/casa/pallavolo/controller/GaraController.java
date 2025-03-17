package casa.pallavolo.controller;

import casa.pallavolo.dto.GaraDTO;
import casa.pallavolo.model.Gara;
import casa.pallavolo.model.Squadra;
import casa.pallavolo.service.GaraService;
import casa.pallavolo.service.SquadraService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(Paths.GARE_BASE)
public class GaraController {

    @Autowired
    private GaraService garaService;
    @Autowired
    private SquadraService squadraService;

    @GetMapping(Paths.GET_ALL_GARE)
    public ResponseEntity<List<GaraDTO>> getAllGare() {
        List<GaraDTO> gare = garaService.getAllGare();
        if (gare.isEmpty()) {
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(gare);
    }

    @GetMapping(Paths.GET_GARE_BY_SQUADRA)
    public ResponseEntity<List<GaraDTO>> getGareBySquadra(@PathVariable Integer idSquadra) {
        List<GaraDTO> gare = garaService.getGareBySquadra(idSquadra);
        if (gare.isEmpty()) {
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(gare);
    }

    @GetMapping(Paths.GET_GARE_CONCLUSE)
    public ResponseEntity<List<GaraDTO>> getGareConcluse(
            @RequestParam(required = false) Integer idSquadra,
            @RequestParam(required = false) Integer anno) {

        List<GaraDTO> gare = garaService.getGareConcluse(idSquadra, anno);

        if (gare.isEmpty()) {
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(gare);
    }


    @PostMapping(Paths.INSERT_GARA)
    public ResponseEntity<?> inserisciGara(@RequestBody GaraDTO garaDaInserire) {
        try {
            Gara garaInserita = garaService.inserisciGara(garaDaInserire);
            return ResponseEntity.ok(garaInserita);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(Paths.UPDATE_GARA)
    public ResponseEntity<GaraDTO> updateGara(@RequestBody GaraDTO garaDaModificare) {
        GaraDTO garaAggiornata = garaService.modificaGara(garaDaModificare);
        return ResponseEntity.ok(garaAggiornata);
    }

    @DeleteMapping(Paths.DELETE_GARA_BY_ID)
    public ResponseEntity<Void> eliminaGaraById(@PathVariable Integer id) {
        garaService.eliminaGaraById(id);
        return GenericUtils.noContentResult();
    }

    @PostMapping(Paths.GENERA_LISTA_GARA)
    public ResponseEntity<ByteArrayResource> generaListaGara(@RequestBody GaraDTO datiGara) {
        byte[] listaGara = null;
        try {
            listaGara = garaService.generaListaGara(datiGara);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        ByteArrayResource resource = new ByteArrayResource(listaGara);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=gara_documento.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(listaGara.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping(Paths.CONDIVIDI_CALENDARIO)
    public ResponseEntity<ByteArrayResource> condividiCalendario(@RequestBody List<GaraDTO> calendario) {
        byte[] calendarioPdf = null;
        try {
            calendarioPdf = garaService.condividiCalendario(calendario);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        ByteArrayResource resource = new ByteArrayResource(calendarioPdf);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=gara_documento.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(calendarioPdf.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping(Paths.CARICA_FILE_CALENDARIO)
    public ResponseEntity<List<Gara>> caricaFileCalendario(@RequestParam("file") MultipartFile file) {
        List<Gara> gare = new ArrayList<>();
        try {
            gare = garaService.caricaCalendarioFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok().body(gare);
    }

    @GetMapping(Paths.COUNT_VITTORIE_BY_SQUADRA)
    public ResponseEntity<Integer> countVittorieBySquadra(@PathVariable Integer idSquadra) {
        Squadra squadra = squadraService.getSquadraById(idSquadra);
        Integer countVittorie = garaService.countVittorieBySquadra(squadra);
        return ResponseEntity.ok(countVittorie);
    }

    @GetMapping(Paths.COUNT_SCONFITTE_BY_SQUADRA)
    public ResponseEntity<Integer> countSconfitteBySquadra(@PathVariable Integer idSquadra) {
        Squadra squadra = squadraService.getSquadraById(idSquadra);
        Integer countSconfitte = garaService.countSconfitteBySquadra(squadra);
        return ResponseEntity.ok(countSconfitte);
    }

    @PutMapping(Paths.ANNULLA_RISULTATO)
    public ResponseEntity<Void> annullaRisultato(@RequestBody GaraDTO garaDaModificare) {
        garaService.annullaRisultato(garaDaModificare);
        return ResponseEntity.ok().build();
    }

    @GetMapping(Paths.GET_ANNI_RAGGRUPPATI)
    public ResponseEntity<List<Integer>> getAnniRaggruppati() {
        List<Integer> anni = garaService.getAnniRaggruppati();
        return ResponseEntity.ok(anni);
    }

}
