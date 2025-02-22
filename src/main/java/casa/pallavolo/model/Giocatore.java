package casa.pallavolo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_giocatori")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Giocatore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String cognome;
	private Integer numeroMaglia;
	private String ruolo;
	@ManyToOne
	@JoinColumn(name = "squadra")
	private Squadra squadra;
	private LocalDate dataNascita;
	private String tesseraUisp;
	private Boolean isCapitano;
}
