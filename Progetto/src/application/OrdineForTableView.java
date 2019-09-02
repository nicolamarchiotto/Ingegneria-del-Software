package application;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrdineForTableView {
	
	private String codiceOrdine;
	private String idAcquirente;
	private String dataAcquisto;
	private String stato;
	private ArrayList<Libro> libri;
	private Ordine ordine;
	private User user;
	
	
	public OrdineForTableView(String codiceOrdine, String idAcquirente, LocalDate dataAcquisto, String stato, ArrayList<Libro> l) {
		this.codiceOrdine=codiceOrdine;
		this.idAcquirente=idAcquirente;
		this.dataAcquisto=dataAcquisto.toString();
		this.stato=stato;
		this.libri=l;
	}
	
	public OrdineForTableView(User u, Ordine o){
		this.user=u;
		this.ordine=o;
		this.codiceOrdine=ordine.getId();
		this.idAcquirente=ordine.getUserId();
		this.dataAcquisto=ordine.getData().toString();
		this.stato=ordine.getStato();
		
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
	
	public ArrayList<Libro> getLibri(){
		return this.libri;
		//this.ordine.getLibri();
	}
	
	public double getPrezzoTot() {
		double sup=0;
		for(Libro b: this.libri)
			sup+=b.getPrezzo();
		return sup;
		//ordine.getTotalCost();
	}
	
	
}
