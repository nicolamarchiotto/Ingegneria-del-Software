package application;

import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<Object>{

	private String nome;
	private String cognome;
	private String indirizzi;
	private String cap;
	private String citta;
	private String telefono;
	private String email;
	private LibroCard librocard=null;
	private String pw;
	private List<Ordine> ordini = null;
	
	/*
	 * Carrello LOCALE
	 */
	private ArrayList<Libro> carrello=new ArrayList<Libro>();
	
	private String identificativoCarta;
	private int puntiCard;
	
	//USER exNovo
	
	public User(String nome, String cognome, String indirizzi, String cap, String citta, String telefono, String email,
			String pw) {
		this.nome=nome;
		this.cognome=cognome;
		this.indirizzi=indirizzi;
		this.cap=cap;
		this.citta=citta;
		this.telefono=telefono;
		this.email=email;
		this.pw=pw;
		this.librocard=new LibroCard(nome,cognome);
		this.identificativoCarta=this.librocard.getId();
		this.puntiCard=this.librocard.getPunti();
	}
	
	//User copiato da DB a local
	
	public User(String nome, String cognome, String indirizzi, String cap, String citta, String telefono, String email,
			String pw, String libroCardId, int puntiLibroCard, int giornoDataEmissione, int meseDataEmissione, int annoDataEmissione, int oraDataEmissione) {
		this.nome=nome;
		this.cognome=cognome;
		this.indirizzi=indirizzi;
		this.cap=cap;
		this.citta=citta;
		this.telefono=telefono;
		this.email=email;
		this.pw=pw;
		if(libroCardId == null) this.librocard = null;
		else {
			this.librocard=new LibroCard(libroCardId, puntiLibroCard, giornoDataEmissione, meseDataEmissione, annoDataEmissione, oraDataEmissione);
			this.identificativoCarta=libroCardId;
			this.puntiCard=this.librocard.getPunti();
		}
		
	}
	
	
	public User(String email, String pw) {
		this.email=email;
		this.pw=pw;
	}
	
	@Override
	public int compareTo(Object other) {
		if( (other instanceof User) &&  (pw.equals(((User)other).pw)) && (email.equals(((User)other).email)) )
			return 0;
		else
			return -1;		
	}
	
	//FIXME DA TESTARE 
	public void setListaOrdini(){
	 	this.ordini=SqliteConnection.getOrderList(SqliteConnection.getFieldOrdine(), this);
	 }
	
	public String getEmail() {
		return this.email;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public String getCognome() {
		return this.cognome;
	}
	
	public String getIndirizzi() {
		return this.indirizzi;
	}
	
	public String getTelefono() {
		return this.telefono;
	}
	
	public List<Ordine> getOrdini(){
		return this.ordini;
	}
	
	//verify->false = email già utilizzata, usane un'altra
	public boolean verifyId(List<User> l) {
		for(User u: l) {
			if(this.email.equals(u.getEmail()))
				return false;
		}
		return true;
	}
	
	
	public boolean equals(Object other) {
		if(this.compareTo(other)==0)
			return true;
		else
			return false;
	}

	public String toString() {
		return this.email + " " +  this.pw ;
	}
	
	public String getPw() {
		return this.pw.toString();
	}

	public LibroCard getLibroCard() {
		return  this.librocard;
	}

	public String getCap() {
		return this.cap;
	}

	public String getCitta() {
		return this.citta;
	}
	
	public String getIdentificativoCarta() {
		return this.identificativoCarta;
	}
	
	public int getPuntiCard() {
		return this.puntiCard;
	}
	
	public void addLibroToCarrello(Libro l) {
		carrello.add(l);		
	}
	
	public void removeLibroFromCarrello(Libro l) {
		carrello.remove(l);
	}
	
	public ArrayList<Libro> getCarrello(){
		return this.carrello;
	}
	
	public String carrelloToString() {
		String sup="";
		
		for(Libro l: this.carrello)
			sup=sup+l.toString()+"\n";
		
		return sup;
	}
}
