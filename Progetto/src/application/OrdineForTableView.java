package application;

import java.time.LocalDateTime;

public class OrdineForTableView {
	
	private String codiceOrdine;
	private String IdAcquirente;
	private String dataAcquisto;
	private String stato;
	
	public OrdineForTableView(String codiceOrdine, String IdAcquirente, LocalDateTime dataAcquisto, String stato) {
		this.codiceOrdine=codiceOrdine;
		this.IdAcquirente=IdAcquirente;
		this.dataAcquisto=dataAcquisto.toString();
		this.stato=stato;
	}
	
	public String toString() {
		return codiceOrdine+" "+IdAcquirente+" "+dataAcquisto+" "+stato;
	}
}
