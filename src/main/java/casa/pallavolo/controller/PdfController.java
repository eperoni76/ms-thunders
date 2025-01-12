package casa.pallavolo.controller;

import casa.pallavolo.dto.DatiGaraDTO;
import casa.pallavolo.service.PdfService;
import casa.pallavolo.utils.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping(Paths.PDF_BASE)
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping(Paths.GENERA_LISTA_GARA)
    public ResponseEntity<ByteArrayResource> generaListaGara(@RequestBody DatiGaraDTO datiGara){
        byte[] listaGara = null;
        try {
            listaGara = pdfService.generaListaGara(datiGara);
        } catch (IOException e) {
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
}
