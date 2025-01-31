package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_gare")
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
    @ManyToOne
    @JoinColumn(name = "id_squadra", referencedColumnName = "id")
    private Squadra squadra;
    private String localita;
    private String impianto;
    private LocalDateTime dataOra;
    private String campionato;
    private Boolean isTrasferta;
}
