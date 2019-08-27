package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Ordine {
	private String id;
	private LocalDateTime data;
	private ArrayList<Libro> libriOrdine;
	private double totalCost=0;
	private String paymentType;
	private int saldoPuntiOrdine=0;
	private String stato;
	//user che ha effettuato l'ordine
	private User user;
	
	Random r=new Random();
	
	public Ordine(User user, String tipoPagamento, Libro... libriCollection) {
		for(Libro l: libriCollection) {
			this.libriOrdine.add(l);
			this.totalCost+=l.getPrezzo();
			this.saldoPuntiOrdine+=l.getPunti();
		}
		this.data=LocalDateTime.now();
		this.user=user;
		this.paymentType=tipoPagamento;
		this.id=getIdOrdine(user.getCitta(), user.getCap());		
	}
	
	private String getIdOrdine(String citta, String cap) {
		int i=1000+r.nextInt(8999);
		
		return citta.substring(0, 1)+cap.substring(0,1) + i;
	}

	

}
