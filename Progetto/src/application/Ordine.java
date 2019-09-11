package application;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ordine {
	private String idOrdine;
	private LocalDateTime data;
	private List<Libro> libriOrdine = new ArrayList<Libro>();
	private double totalCost=0;
	private String paymentType;
	private int saldoPuntiOrdine=0;
	//user che ha effettuato l'ordine
	private String idUser;
	private String indirizzoSpedizione = null;
	
	Random r=new Random();
	
	public Ordine(String idUser, String tipoPagamento, String indirizzoSpedizione, ArrayList<Libro> libriCollection) {
		for(Libro l: libriCollection) {
			this.libriOrdine.add(l);
			this.totalCost+=(l.getPrezzo()*l.getCopieVenduteNelSingoloOrdine());
			this.saldoPuntiOrdine+=l.getPunti();
		}
		this.data=LocalDateTime.now();
		this.idUser=idUser;
		this.paymentType=tipoPagamento;
		this.idOrdine=getIdOrdine(tipoPagamento, idUser);
		this.indirizzoSpedizione=indirizzoSpedizione;
	}
	
	
	//costruttore per il pescare dal db
	public Ordine(String id, int giorno, int mese, int anno, int ora, List<Libro> bookList, double totalCost, String paymentType, int saldoPuntiOrdine, String userId, String indirizzoSpedizione) {
		this.idOrdine = id;
		this.data = LocalDateTime.of(anno, mese, giorno, ora, 0);
		this.libriOrdine = bookList;
		this.totalCost = totalCost;
		this.paymentType = paymentType;
		this.saldoPuntiOrdine = saldoPuntiOrdine;
		this.idUser = userId;
		this.indirizzoSpedizione = indirizzoSpedizione;
	}
	
	private String getIdOrdine(String payment, String userId) {
		int i=1000+r.nextInt(8999);
		
		return i+payment.substring(0, 1)+userId.substring(0,1);
	}
	
	public String toString() {
		return this.idOrdine+" "+this.idUser+" "+this.indirizzoSpedizione;
	}
	
	public String getStato() {
		
		long daysBetween = ChronoUnit.HOURS.between(data, LocalDateTime.now());
		
		if(daysBetween==0)
			return "non ancora spedito";
		else if(daysBetween==1)
			return "spedito";
		else
			return "consegnato";
	}
	
	public String getIdOrdine() {
		return this.idOrdine;
	}
	
	public LocalDateTime getData() {
		return this.data;
	}
	
	public String getUserId() {
		return this.idUser;
	}
	
	public List<Libro> getLibri(){
		return this.libriOrdine;
	}
	
	public double getTotalCost() {
		return this.totalCost;
	}
	
	public String getPaymentType() {
		return this.paymentType;
	}
	
	public int getSaldoPuntiOrdine() {
		return this.saldoPuntiOrdine;
	}
	
	public String getIndirizzoSpedizione() {
		return this.indirizzoSpedizione;
	}
	
	//metodo per trasformare un ordine in userLess nel caso in cui l'utente "padrone" dell'ordine voglia essere cancellato dal sistema
	public void becomeUserLess() {
		this.idUser = "";
	}

	

}
