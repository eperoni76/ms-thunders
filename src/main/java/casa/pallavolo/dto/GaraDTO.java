package casa.pallavolo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GaraDTO {
    private Integer numeroGara;
    private String ospitante;
    private String ospite;
    private String localita;
    private String impianto;
    private LocalDateTime dataOra;
    private String campionato;
    private Boolean isTrasferta;
    private Integer allenatore;
    private Integer secondoAllenatore;
    private Integer dirigente;
    private Integer addettoDefibrillatore;
}
