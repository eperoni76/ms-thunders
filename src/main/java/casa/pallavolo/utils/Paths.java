package casa.pallavolo.utils;

public class Paths {
	public final static String DIRIGENTI_BASE = "/dirigenti";
	public final static String ADDETTI_BASE = "/addetti";
	public final static String SQUADRE_BASE = "/squadre";
	public final static String GARE_BASE = "/gare";


	public final static String GET_GIOCATORI = "/getGiocatori";
	public final static String GET_GIOCATORE_BY_ID = "/getGiocatoreById/{id}";
	public final static String GET_GIOCATORI_BY_RUOLO = "/getGiocatoriByRuolo/{ruolo}";
	public final static String GET_GIOCATORI_BY_SQUADRA = "/getGiocatoriBySquadra/{squadra}";
	public final static String GET_ALL_DIRIGENTI = "/getAllDirigenti";
	public final static String GET_ALL_ALLENATORI = "/getAllAllenatori";
	public final static String GET_ALL_ADDETTI_DEFIBRILLATORE = "/getAllAddettiDefibrillatore";
	public final static String GET_ALL_SQUADRE = "/getAllSquadre";
	public final static String GET_SQUADRA_BY_ID = "/getSquadraById/{idSquadra}";
	public final static String GET_ALL_GARE = "/getAllGare";
	public final static String GET_GARE_BY_SQUADRA = "/getGareBySquadra/{idSquadra}";
	public final static String GET_GARE_CONCLUSE = "/getGareConcluse";
	public static final String GET_GARE_CONCLUSE_BY_SQUADRA = "/getGareConcluseBySquadra/{idSquadra}";
	public static final String GET_GARE_CONCLUSE_BY_ANNO = "/getGareConcluseByAnno/{anno}";

	public final static String INSERT_GIOCATORE = "/inserisciGiocatore";
	public final static String INSERT_GARA = "/inserisciGara";

	public final static String DELETE_GIOCATORE_BY_ID = "/deleteGiocatoreById/{id}";
	public static final String DELETE_GARA_BY_ID = "/deleteGaraById/{id}";

	public final static String UPDATE_GIOCATORE = "/updateGiocatore";
	public static final String UPDATE_GARA = "/updateGara";

	public final static String GENERA_LISTA_GARA = "/listaGara";

	//Path per immagini UISP creazione lista gara
	public final static String UISP_IMAGE_1 = "src/main/resources/images/uisp1.png";
	public final static String UISP_IMAGE_2 = "src/main/resources/images/uisp2.png";
	public final static String UISP_IMAGE_3 = "src/main/resources/images/uisp3.jpg";
}
