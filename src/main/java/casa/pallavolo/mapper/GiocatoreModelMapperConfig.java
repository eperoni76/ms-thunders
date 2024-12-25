package casa.pallavolo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GiocatoreModelMapperConfig {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
