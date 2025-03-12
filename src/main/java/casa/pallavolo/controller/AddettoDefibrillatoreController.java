package casa.pallavolo.controller;

import casa.pallavolo.dto.AddettoDefibrillatoreDTO;
import casa.pallavolo.service.AddettoDefibrillatoreService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(Paths.ADDETTI_BASE)
public class AddettoDefibrillatoreController {

    @Autowired
    private AddettoDefibrillatoreService addettoDefibrillatoreService;

    @GetMapping(Paths.GET_ALL_ADDETTI_DEFIBRILLATORE)
    public ResponseEntity<List<AddettoDefibrillatoreDTO>> getAllAddettiDefibrillatore() {
        List<AddettoDefibrillatoreDTO> addetti = addettoDefibrillatoreService.getAllAddetti();
        if (addetti.isEmpty()) {
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(addetti);
    }

    @PostMapping(Paths.INSERT_ADDETTO_DEFIBRILLATORE)
    public ResponseEntity<AddettoDefibrillatoreDTO> insertAddetto(@RequestBody AddettoDefibrillatoreDTO addetto) {
        try {
            if (addetto.getNome() == null || addetto.getNome().isEmpty()) {
                throw new Exception("Il nome dell'addetto è obbligatorio.");
            }
            AddettoDefibrillatoreDTO nuovoAddetto = addettoDefibrillatoreService.saveAddetto(addetto);
            return ResponseEntity.ok(nuovoAddetto);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'inserimento dell'addetto.", e);
        }
    }

    @PutMapping(Paths.UPDATE_ADDETTO_DEFIBRILLATORE)
    public ResponseEntity<AddettoDefibrillatoreDTO> updateAddetto(@RequestBody AddettoDefibrillatoreDTO addetto) {
        try {
            if (addetto.getId() == null) {
                throw new Exception("L'ID dell'addetto è obbligatorio per l'aggiornamento.");
            }
            AddettoDefibrillatoreDTO addettoAggiornato = addettoDefibrillatoreService.updateAddetto(addetto);
            return ResponseEntity.ok(addettoAggiornato);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'addetto.", e);
        }
    }

    @DeleteMapping(Paths.DELETE_ADDETTO_DEFIBRILLATORE)
    public ResponseEntity<Void> deleteAddetto(@PathVariable Integer id) {
        try {
            if (!addettoDefibrillatoreService.existsById(id)) {
                throw new Exception("L'addetto con ID " + id + " non esiste.");
            }
            addettoDefibrillatoreService.deleteAddettoById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'addetto.", e);
        }
    }
}
