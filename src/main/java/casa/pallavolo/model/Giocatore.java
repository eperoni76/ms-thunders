package casa.pallavolo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_thunders")
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
	private String squadra;
	private LocalDate dataDiNascita;
	private String tesseraUisp;
}
