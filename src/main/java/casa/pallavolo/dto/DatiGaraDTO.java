package casa.pallavolo.dto;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate dataOra;
    private String campionato;
}
