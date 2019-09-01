package application;

import java.util.Random;

import javafx.beans.property.SimpleStringProperty;

public class Libro implements Comparable<Object>{
	private SimpleStringProperty titolo;
	private SimpleStringProperty autori;
	private SimpleStringProperty casaeditrice;
	private int annopubblicazione;
	private SimpleStringProperty isbn;
	private SimpleStringProperty genere;
	private double prezzo;
	private SimpleStringProperty brevedescrizione;
	private int copieVendute=0;
	private int punti;
	
	static Random r=new Random();
	
	//costruttore per creare libro ex novo, crea anche ISBN casuale
	//da rivedere isbn, genera anche numeri negativi
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			String genere, double prezzo, String brevedescrizione, int punti) {
		this.titolo=new SimpleStringProperty(titolo);
		this.autori=new SimpleStringProperty(autori);
		this.casaeditrice=new SimpleStringProperty(casaeditrice);
		this.annopubblicazione=annopubblicazione;
		this.isbn=new SimpleStringProperty(Long.toString(r.nextLong()));
		this.genere=new SimpleStringProperty(genere);
		this.prezzo=prezzo;
		this.brevedescrizione=new SimpleStringProperty(brevedescrizione);
		this.punti=punti;
	}
	
	
	//costruttore per aggiungere da DB a locale
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			String isbn, String genere, double prezzo, String brevedescrizione, int punti) {
		
		this.titolo=new SimpleStringProperty(titolo);
		this.autori=new SimpleStringProperty(autori);
		this.casaeditrice=new SimpleStringProperty(casaeditrice);
		this.annopubblicazione=annopubblicazione;
		this.isbn=new SimpleStringProperty(isbn);
		this.genere=new SimpleStringProperty(genere);
		this.prezzo=prezzo;
		this.brevedescrizione=new SimpleStringProperty(brevedescrizione);
		this.punti=punti;
	}


	@Override
	public int compareTo(Object other) {
		if(other instanceof Libro)
			return (this.isbn).toString().compareTo(((Libro) other).getIsbn().toString());
		else
			return -1;
		
	}
	
	
	public String getIsbn() {
		return this.isbn.get();
	}
	
	public String getTitolo() {
		return titolo.get();
	}
	
	public String getAutore() {
		return autori.get();
	}
	public double getPrezzo() {
		return prezzo;
	}
	public String getCasaEditrice() {
		return this.casaeditrice.get();
	}
	public int getAnnoPublicazione() {
		return this.annopubblicazione;
	}
	public String getGenere() {
		return this.genere.get();
	}
	public String getBreveDescrizione() {
		
		int i=0;
		String sup;
		String result="";
		String brevDescr=this.brevedescrizione.get();
		
		for(i=0;i<this.brevedescrizione.get().length();i=i+30) {
			sup=brevDescr.substring(i, i+30);
			result=sup+"\n";
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

	@Override
	public String toString() {
		return this.isbn + " " +  this.titolo;
	}
	
	public String toStringLong() {
		return titolo + " " +autori + " " + prezzo;
	}
	

	
}