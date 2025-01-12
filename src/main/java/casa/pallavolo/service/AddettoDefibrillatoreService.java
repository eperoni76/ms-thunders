package casa.pallavolo.service;

import casa.pallavolo.model.AddettoDefibrillatore;
import casa.pallavolo.repository.AddettoDefibrillatoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddettoDefibrillatoreService {

    @Autowired
    private AddettoDefibrillatoreRepository addettoDefibrillatoreRepository;

    public AddettoDefibrillatore getAddettoDefibrillatoreById(Integer id){
        return addettoDefibrillatoreRepository.findById(id).orElse(null);
    }

    public List<AddettoDefibrillatore> getAllAddetti(){
        return addettoDefibrillatoreRepository.findAll();
    }
}
