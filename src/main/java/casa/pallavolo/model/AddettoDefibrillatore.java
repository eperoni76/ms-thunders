package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_addetto_defibrillatore")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddettoDefibrillatore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private LocalDate dataNascita;
    private String luogoNascita;
    private String luogoResidenza;

}
