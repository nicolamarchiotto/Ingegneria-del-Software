package application;

import java.time.LocalDate;

public class OrdineForTableView {
	
	private String codiceOrdine;
	private String idAcquirente;
	private String dataAcquisto;
	private String stato;
	
	public OrdineForTableView(String codiceOrdine, String idAcquirente, LocalDate dataAcquisto, String stato) {
		this.codiceOrdine=codiceOrdine;
		this.idAcquirente=idAcquirente;
		this.dataAcquisto=dataAcquisto.toString();
		this.stato=stato;
	}
	
	public String toString() {
		return codiceOrdine+" "+idAcquirente+" "+dataAcquisto+" "+stato;
	}
	
	public String getIdAcquirente() {
		return this.idAcquirente;
	}
	
	public String getCodiceOrdine() {
		return this.codiceOrdine;
	}
	
	public String getDataAcquisto() {
		return this.dataAcquisto;
	}
	
	public String getStato() {
		return this.stato;
	}
}
