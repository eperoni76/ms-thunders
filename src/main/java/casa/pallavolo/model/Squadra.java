package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_squadre")
@Data
public class Squadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeSquadra;
    private String campionato;
}
