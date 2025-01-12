package casa.pallavolo.service;

import casa.pallavolo.dto.AddettoDefibrillatoreDTO;
import casa.pallavolo.model.AddettoDefibrillatore;
import casa.pallavolo.repository.AddettoDefibrillatoreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddettoDefibrillatoreService {

    @Autowired
    private AddettoDefibrillatoreRepository addettoDefibrillatoreRepository;
    @Autowired
    private ModelMapper addettoMapper;

    public AddettoDefibrillatore getAddettoDefibrillatoreById(Integer id){
        return addettoDefibrillatoreRepository.findById(id).orElse(null);
    }

    public List<AddettoDefibrillatoreDTO> getAllAddetti(){
        return addettoDefibrillatoreRepository.findAll()
                .stream()
                .map(addetto -> addettoMapper.map(addetto, AddettoDefibrillatoreDTO.class))
                .sorted((ad1, ad2) -> ad1.getCognome().compareTo(ad2.getCognome()))
                .toList();
    }
}
