package casa.pallavolo.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddettoDefibrillatoreDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private LocalDate dataNascita;
    private String luogoNascita;
    private String luogoResidenza;
}
