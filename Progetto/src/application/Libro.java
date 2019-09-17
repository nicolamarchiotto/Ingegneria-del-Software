package application;

import java.util.Random;
import java.lang.Math;

public class Libro implements Comparable<Object>{
	private String titolo;
	private String autore;
	private String casaeditrice;
	private int annopubblicazione;
	private String isbn;
	private String genere;
	private double prezzo;
	private String brevedescrizione;
	private int copieVenduteTotali = 0;
	private int punti;
	private int copieVenduteNelSingoloOrdine=0;
	
	//campo per capire se un libro è stato cancellato o meno
	private int disponibilita;
	
	static Random r=new Random();
	
	//costruttore per creare libro ex novo, crea anche ISBN casuale
	//da rivedere isbn, genera anche numeri negativi
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			String genere, double prezzo, String brevedescrizione, int punti) {
		this.titolo=titolo;
		this.autore=autori;
		this.casaeditrice=casaeditrice;
		this.annopubblicazione=annopubblicazione;
		this.genere=genere;
		this.prezzo=prezzo;
		this.brevedescrizione=brevedescrizione;
		this.punti=punti;
		this.copieVenduteTotali=0;
		//nextLong può restituire anche valori negativi
		long sup=r.nextLong();
		if(sup<0)
			sup=sup*-1;
		this.isbn=Long.toString(Math.abs(sup));
		
		//disponibile dato che appena creato
		this.disponibilita = 1;
	}
	
	
	//costruttore per aggiungere da DB a locale
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			String isbn, String genere, double prezzo, String brevedescrizione, int copieVenduteNelSingoloOrdine, int copieVenduteTotali, int punti, int disponibilita) {
		
		this.titolo=titolo;
		this.autore=autori;
		this.casaeditrice=casaeditrice;
		this.annopubblicazione=annopubblicazione;
		this.isbn=isbn;
		this.genere=genere;
		this.prezzo=prezzo;
		this.brevedescrizione=brevedescrizione;
		this.copieVenduteNelSingoloOrdine = copieVenduteNelSingoloOrdine;
		this.copieVenduteTotali=copieVenduteTotali;
		this.punti = punti;
		this.disponibilita = disponibilita;
	}
	
	

	@Override
	public int compareTo(Object other) {
		if(other instanceof Libro)
			return (this.isbn).toString().compareTo(((Libro) other).getIsbn().toString());
		else
			return -1;
		
	}
	
	
	public String getIsbn() {
		return this.isbn;
	}
	
	public String getTitolo() {
		return titolo;
	}
	
	public String getAutore() {
		return autore;
	}
	public double getPrezzo() {
		return prezzo;
	}
	public String getCasaEditrice() {
		return this.casaeditrice;
	}
	public int getAnnoPubblicazione() {
		return this.annopubblicazione;
	}
	public String getGenere() {
		return this.genere;
	}
	
	public String getBreveDescrizione() {
		return this.brevedescrizione;
	}
	
	public String getBreveDescrizionePortataACapoOgniTotCaratteri(int tot) {
		
		int i=0;
		String sup="";
		String result="";
		String brevDescr=this.brevedescrizione;
		int length=this.brevedescrizione.length();
		int giri=1;
		for(i=0;i<length;i=i+tot) {
			if(length> (giri*tot) ) {
				sup=brevDescr.substring(i, i+tot);
				result+=(sup+"\n");
				giri++;
			}
			else {
				sup=brevDescr.substring(i, length);
				result+=(sup+"\n");
			}
		}
		
		return result;
	}
	
	public int getPunti() {
		return this.punti;
	}
	
	public boolean equals(Object other) {
		if(this.compareTo(other)==0)
			return true;
		else
			return false;
	}
	
	public int getCopieVendute() {
		return this.copieVenduteTotali;
	}
	
	public int getDisponibilita() {
		return this.disponibilita;
	}
	
	
	public String toString() {
		return "Stampa Libro, titolo: "+titolo + " autore: " +autore + " prezzo:" + prezzo;
	}
	
	public void aggiungiCopieAlSingoloOrdine(int numCopie) {
		this.copieVenduteNelSingoloOrdine += numCopie;
		this.copieVenduteTotali += numCopie;
	}
	
	public void setToZeroCopieVenduteSingoloOrdine() {
		this.copieVenduteNelSingoloOrdine=0;
	}
	
	public int getCopieVenduteNelSingoloOrdine() {
		return this.copieVenduteNelSingoloOrdine;
	}
	
	public void setCopieVenduteSingoloOrdine(int copie) {
		this.copieVenduteNelSingoloOrdine=copie;
		this.copieVenduteTotali += copie;
	}
	
	public void setToZeroCopieVendute() {
		this.copieVenduteTotali = 0;
	}

	
}