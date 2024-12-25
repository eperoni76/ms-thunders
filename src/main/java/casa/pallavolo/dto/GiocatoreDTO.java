package casa.pallavolo.dto;

public class GiocatoreDTO {
	private Integer id;
	private String nome;
	private String cognome;
	private Integer numeroMaglia;
	private String ruolo;
	
	public GiocatoreDTO() {}
	
	public GiocatoreDTO(Integer id, String nome, String cognome, Integer numeroMaglia, String ruolo) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.numeroMaglia = numeroMaglia;
		this.ruolo = ruolo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Integer getNumeroMaglia() {
		return numeroMaglia;
	}

	public void setNumeroMaglia(Integer numeroMaglia) {
		this.numeroMaglia = numeroMaglia;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
}
