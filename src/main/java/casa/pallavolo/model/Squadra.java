package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_squadre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Squadra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomeSquadra;
    private String campionato;
    private String nomeUfficiale;

    public Squadra(Integer id) {
        this.id = id;
    }
}
