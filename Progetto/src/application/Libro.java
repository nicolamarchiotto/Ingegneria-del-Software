package application;

public class Libro implements Comparable<Object>{
	private String titolo;
	private String autori;
	private String casaeditrice;
	private int annopubblicazione;
	private String isbn;
	private String genere;
	private String brevedescrizione;
	private int posizione;
	private int punti;
	
	
	public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione, String isbn, String genere,
			String brevedescrizione, int posizone, int punti) {
		titolo=titolo;
		autori=autori;
		casaeditrice=casaeditrice;
		annopubblicazione=annopubblicazione;
		isbn=isbn;
		genere=genere;
		brevedescrizione=brevedescrizione;
		posizione=posizione;
		punti=punti;
	}


	@Override
	public int compareTo(Object other) {
		if(other instanceof Libro)
			return (this.isbn).compareTo(((Libro) other).getIsbn());
		else
			return -1;
		
	}
	
	public String getIsbn() {
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
}