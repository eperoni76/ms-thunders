package casa.pallavolo.dto;

import casa.pallavolo.model.Squadra;
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
	private Squadra squadra;
	private LocalDate dataNascita;
	private String tesseraUisp;
	private Boolean isCapitano;

}
