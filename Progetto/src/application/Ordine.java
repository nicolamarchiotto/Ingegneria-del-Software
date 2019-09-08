package application;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ordine {
	private String id;
	private LocalDateTime data;
	private List<Libro> libriOrdine = new ArrayList<Libro>();
	private double totalCost=0;
	private String paymentType;
	private int saldoPuntiOrdine=0;
	//user che ha effettuato l'ordine
	private String idUser;
	
	Random r=new Random();
	
	public Ordine(User user, String tipoPagamento, Libro ... libriCollection) {
		for(Libro l: libriCollection) {
			this.libriOrdine.add(l);
			this.totalCost+=(l.getPrezzo()*l.getCopieVenduteNelSingoloOrdine());
			this.saldoPuntiOrdine+=l.getPunti();
		}
		this.data=LocalDateTime.now();
		this.idUser=user.getEmail();
		this.paymentType=tipoPagamento;
		this.id=getIdOrdine(user.getCitta(), user.getCap());
		this.idUser=user.getEmail();
	}
	
	
	//costruttore per il pescare dal db
	public Ordine(String id, int giorno, int mese, int anno, int ora, List<Libro> bookList, double totalCost, String paymentType, int saldoPuntiOrdine, String userId) {
		this.id = id;
		this.data = LocalDateTime.of(anno, mese, giorno, ora, 0);
		this.libriOrdine = bookList;
		this.totalCost = totalCost;
		this.paymentType = paymentType;
		this.saldoPuntiOrdine = saldoPuntiOrdine;
		this.idUser = userId;
	}
	
	private String getIdOrdine(String citta, String cap) {
		int i=1000+r.nextInt(8999);
		
		return citta.substring(0, 1)+cap.substring(0,1) + i;
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
	
	public String getId() {
		return this.id;
	}
	
	public LocalDateTime getData() {
		return this.data;
	}
	
	//FIXME PROVA
	public void addCosto() {
		this.totalCost= 69; //mmh
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

	

}
