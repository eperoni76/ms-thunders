package casa.pallavolo.utils;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.ResponseEntity;
@CommonsLog
public class GenericUtils {
    public static <T> ResponseEntity<T> noContentResult() {
        log.info("Nessun risultato trovato");
        return ResponseEntity.noContent().build();
    }
}
