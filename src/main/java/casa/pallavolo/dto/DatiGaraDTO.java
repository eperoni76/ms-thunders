package casa.pallavolo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DatiGaraDTO {
    private Integer numeroGara;
    private String ospitante;
    private String ospite;
    private String localita;
    private String impianto;
    private LocalDateTime dataOra;
    private String campionato;
}
