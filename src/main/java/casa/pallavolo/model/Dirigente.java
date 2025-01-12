package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_dirigenti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dirigente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cognome;
    private String tesseraUisp;
    private Boolean allenatore;
}
