package casa.pallavolo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SquadraDTO {
    private Integer id;
    private String nomeSquadra;
    private String campionato;

    public SquadraDTO(Integer id) {
        this.id = id;
    }
}
