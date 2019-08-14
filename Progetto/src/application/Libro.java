package application;

public class Libro {
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
}