package application;

public class UserForTableView {
	
	private String idLibroCard;
	private String idUtente;
	private String cognomeId;
	private String nomeId;
	private int puntiCard;
	
	
	
	public UserForTableView(String idLibroCard, String idUtente, String cognomeId, String nomeId, int puntiCard) {
		this.idLibroCard=idLibroCard;
		this.idUtente=idUtente;
		this.cognomeId=cognomeId;
		this.nomeId=nomeId;
		this.puntiCard=puntiCard;
	}
	
	public String getIdUtente() {
		return this.idUtente;
	}
	
	public String getIdLibroCard() {
		return this.idLibroCard;
	}
	
	public String getCognomeId() {
		return this.cognomeId;
	}
	
	public String getNomeId() {
		return this.nomeId;
	}
	
	public int getPuntiCard() {
		return this.puntiCard;
	}
}
