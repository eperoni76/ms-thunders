package casa.pallavolo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiocatoreDTO {
	private Integer id;
	private String nome;
	private String cognome;
	private Integer numeroMaglia;
	private String ruolo;
	private Integer squadra;
	private LocalDate dataNascita;
	private String tesseraUisp;
	private Boolean isCapitano;

}
