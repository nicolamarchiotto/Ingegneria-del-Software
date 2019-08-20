package application;

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
	private int posizione;
	private int punti;
	
	
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			String isbn, String genere, double prezzo, String brevedescrizione, int posizione, int punti) {
		this.titolo=new SimpleStringProperty(titolo);
		this.autori=new SimpleStringProperty(autori);
		this.casaeditrice=new SimpleStringProperty(casaeditrice);
		this.annopubblicazione=annopubblicazione;
		this.isbn=new SimpleStringProperty(isbn);
		this.genere=new SimpleStringProperty(genere);
		this.prezzo=prezzo;
		this.brevedescrizione=new SimpleStringProperty(brevedescrizione);
		this.posizione=posizione;
		this.punti=punti;
	}


	@Override
	public int compareTo(Object other) {
		if(other instanceof Libro)
			return (this.isbn).toString().compareTo(((Libro) other).getIsbn().toString());
		else
			return -1;
		
	}
	
	public SimpleStringProperty getIsbn() {
		return this.isbn;
	}
	
	public boolean equals(Object other) {
		if(this.compareTo(other)==0)
			return true;
		else
			return false;
	}

	public String toString() {
		return this.isbn + " " +  this.titolo;
	}
	
	public String toStringLong() {
		return titolo + " " +autori + " " + prezzo;
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
	
	
}