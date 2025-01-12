package casa.pallavolo.utils;

import org.springframework.http.ResponseEntity;

public class GenericUtils {
    public static <T> ResponseEntity<T> noContentResult() {
        System.out.println("Nessun risultato trovato");
        return ResponseEntity.noContent().build();
    }
}
