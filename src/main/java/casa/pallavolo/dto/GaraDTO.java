package casa.pallavolo.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GaraDTO {
    private Integer id;
    private Integer numeroGara;
    private SquadraDTO squadra;
    private String ospitante;
    private String ospite;
    private String indirizzo;
    private LocalDate data;
    private LocalTime ora;
    private String campionato;
    private Boolean isTrasferta;
    private Boolean isVittoria;
    private Integer allenatore;
    private Integer secondoAllenatore;
    private Integer dirigente;
    private Integer addettoDefibrillatore;
    private String risultato;
    private List<GiocatoreDTO> giocatoriInLista;
}
