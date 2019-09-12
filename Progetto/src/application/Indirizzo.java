package application;

public class Indirizzo {
	private String via;
	private String citta;
	private String cap;
	
	public Indirizzo(String via, String citta, String cap) {
		this.via=via;
		this.citta=citta;
		this.cap=cap;
	}
	
	public String getVia(){
		return this.via;
	}
	
	public String getCitta(){
		return this.citta;
	}
	
	public String getCap() {
		return this.cap;
	}
}
