package application;

import java.time.LocalDateTime;
import java.util.Random;

public class LibroCard {

	private String id;
	private int punti=0;
	private LocalDateTime dataEmissione;
	
	Random r = new Random();
	
	public LibroCard(String nome, String cognome) {
		dataEmissione=LocalDateTime.now();
		id=getIdLibroCard(nome, cognome);
	}

	private String getIdLibroCard(String nome, String cognome) {
		int i=1000+r.nextInt(8999);
		
		return cognome.substring(0, 1)+nome.substring(0,1) + i;
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
