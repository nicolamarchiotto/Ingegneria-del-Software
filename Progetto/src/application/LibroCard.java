package application;

import java.time.LocalDateTime;
import java.util.Random;

public class LibroCard {

	private String id;
	private int punti;
	private LocalDateTime dataEmissione;
	
	Random r = new Random();
	
	public LibroCard(String nome, String cognome) {
		dataEmissione=LocalDateTime.now();
		id=getIdLibroCard(nome, cognome);
		punti=0;
	}
	
	public LibroCard(String id, int punti, int giorno, int mese, int anno, int ora) {
		this.id = id;
		this.punti = punti;
		this.dataEmissione = LocalDateTime.of(anno, mese, giorno, ora, 0, 0, 0);
	}

	private String getIdLibroCard(String nome, String cognome) {
		int i=1000+r.nextInt(8999);
		
		return cognome.substring(0, 1)+nome.substring(0,1) + i;
	}
	
	public void aggiungiPunti(int punti) {
		this.punti+=punti;
	}

	public String getId() {
		return this.id;
	}
	
	public int getPunti() {
		return this.punti;
	}
	
	public LocalDateTime getDataEmissione() {
		return this.dataEmissione;
	}
}
