package casa.pallavolo.controller;

import casa.pallavolo.dto.AddettoDefibrillatoreDTO;
import casa.pallavolo.service.AddettoDefibrillatoreService;
import casa.pallavolo.utils.GenericUtils;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(Paths.ADDETTI_BASE)
public class AddettoDefibrillatoreController {
    @Autowired
    private AddettoDefibrillatoreService addettoDefibrillatoreService;

    @GetMapping(Paths.GET_ALL_ADDETTI_DEFIBRILLATORE)
    public ResponseEntity<List<AddettoDefibrillatoreDTO>> getAllAddettiDefibrillatore(){
        List<AddettoDefibrillatoreDTO> addetti = addettoDefibrillatoreService.getAllAddetti();
        if(addetti.isEmpty()){
            return GenericUtils.noContentResult();
        }
        return ResponseEntity.ok(addetti);
    }
}
