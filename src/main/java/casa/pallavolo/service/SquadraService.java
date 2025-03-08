package casa.pallavolo.service;

import casa.pallavolo.dto.SquadraDTO;
import casa.pallavolo.model.Squadra;
import casa.pallavolo.repository.SquadraRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Service
public class SquadraService {
    @Autowired
    private SquadraRepository squadraRepository;
    @Autowired
    private ModelMapper squadraMapper;

    private static final String IMAGE_FOLDER = "src/main/resources/images/";
    private static final String PLACEHOLDER_IMAGE = IMAGE_FOLDER + "placeholder.png";

    public List<SquadraDTO> getAllSquadre(){
        return squadraRepository
                .findAll()
                .stream()
                .map(squadra -> squadraMapper.map(squadra, SquadraDTO.class))
                .toList();
    }

    public Squadra getSquadraById(Integer id){
        return squadraRepository.findById(id).orElse(null);
    }

    public Squadra insertSquadra(SquadraDTO squadraDTO, MultipartFile immagine) {
        Squadra squadra = new Squadra();
        squadra.setNomeSquadra(squadraDTO.getNomeSquadra());
        squadra.setNomeUfficiale(squadraDTO.getNomeUfficiale());
        squadra.setCampionato(squadraDTO.getCampionato());

        squadra = squadraRepository.save(squadra);
        salvaImmagine(squadra.getNomeSquadra(), immagine);

        return squadra;
    }

    public ByteArrayResource getImmagineSquadra(String nomeSquadra) {
        try {
            Path path = Paths.get(IMAGE_FOLDER + nomeSquadra + ".jpg");

            if (!Files.exists(path)) {
                path = Paths.get(PLACEHOLDER_IMAGE);
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return resource;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private void salvaImmagine(String nomeSquadra, MultipartFile immagine) {
        if (immagine != null && !immagine.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/images/";
                String fileName = nomeSquadra + ".jpg";
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, immagine.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Errore nel salvataggio dell'immagine", e);
            }
        }
    }
}
