package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tb_gare")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Gara {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer numeroGara;
    private String ospitante;
    private String ospite;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_squadra", referencedColumnName = "id")
    private Squadra squadra;
    private String indirizzo;
    private LocalDate data;
    private LocalTime ora;
    private String campionato;
    private Boolean isTrasferta;
    private String risultato;
}
