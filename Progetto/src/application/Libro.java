package application;

import javafx.beans.property.SimpleStringProperty;

public class Libro implements Comparable<Object>{
	public SimpleStringProperty titolo;
	public SimpleStringProperty autori;
	public SimpleStringProperty casaeditrice;
	public int annopubblicazione;
	public SimpleStringProperty isbn;
	public SimpleStringProperty genere;
	public double prezzo;
	public SimpleStringProperty brevedescrizione;
	public int posizione;
	public int punti;
	
	
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