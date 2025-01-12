package casa.pallavolo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DirigenteDTO {
    private Integer id;
    private String nome;
    private String cognome;
    private String tesseraUisp;
    private Boolean allenatore;
}
