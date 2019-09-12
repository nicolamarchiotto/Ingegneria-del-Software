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
		this.ordini = new ArrayList<Ordine>();
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
		this.setListaOrdini();
		
		//FIXME testArea
		//System.out.println(this.ordini.size() + "    " + this.ordini.toString());
	}
	
	public User(String indirizzi, String cap, String citta) {
		this.nome="utente non registrato";
		this.cognome="utente non registrato";
		this.indirizzi=indirizzi;
		this.cap=cap;
		this.citta=citta;
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
		System.out.println("-----FETCHING ORDERS FOR " + this + "-----");
	 	this.ordini=SqliteConnection.getOrderList(this);
	 }
	
	//FIXME PER TESTARE
	public void addOrder(Ordine order) {
		this.ordini.add(order);
	}
	
	public void aggiungiPunti(int punti) {
		this.puntiCard+=punti;
		this.librocard.aggiungiPunti(punti);
	}
	
	public String getIndirizzi() {
		return this.indirizzi;
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
	
	public double getTotalCostFromCarrello() {
		double sup=0;
		for(Libro l: this.carrello) {
			sup+=l.getPrezzo();
		}
		return sup;
	}
	
	public String carrelloToString() {
		String sup="";
		
		for(Libro l: this.carrello)
			sup=sup+(l.toString()+"\n");
		
		return sup;
	}
	
	//Name
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome=nome;
	}
	
	//Surname
	
	public String getCognome() {
		return this.cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome=cognome;
	}
	
	//Address
	
	public String getIndirizzoResidenzaFormattato() {
		return this.indirizzi.split("%")[0] + ", " + this.citta.split("%")[0] + ", " + this.cap.split("%")[0];
	}
	
	
	public String getIndirizzoResidenza() {
		return this.indirizzi.split("%")[0]; 
	}
	
	public void setIndirizzoResidenza(String residence) {
		int pos=this.indirizzi.split("%")[0].length();
		String otherAddress=this.indirizzi.substring(pos, this.indirizzi.length());
		this.indirizzi=(residence+"%"+otherAddress);
	}
	
	public String getCittaResidenza() {
		return this.citta.split("%")[0];
	}
	
	public void setCittaResidenza(String residence) {
		int pos=this.citta.split("%")[0].length();
		String otherCities=this.citta.substring(pos, this.citta.length());
		this.citta=(residence+"%"+otherCities);
	}

	public String getCapResidenza() {
		return this.cap.split("%")[0];
	}
	
	public void setCapResidenza(String residence) {
		int pos=this.cap.split("%")[0].length();
		String otherCap=this.cap.substring(pos, this.cap.length());
		this.cap=(residence+"%"+otherCap);
	}
	
	
	
	//metodi per la gestione di indirizzi
	public List<String> getIndirizziFormattati(){
		List<String> indirizziCompleti = new ArrayList<String>();
			
		String[] singoliIndirizzi = this.indirizzi.split("%");
		String[] singoleCitta = this.citta.split("%");
		String[] singoliCAP = this.cap.split("%");
			
		if(singoleCitta.length != singoliCAP.length || singoleCitta.length != singoliIndirizzi.length || singoliCAP.length != singoliIndirizzi.length) {
			AlertBox.display("ERROR", "Inconsistenza tra dati: length di città, cap, indirizzo");
			return null;
		}
			
		for(int i = 0; i < singoliIndirizzi.length; i++) {
			indirizziCompleti.add(singoliIndirizzi[i] + ", " + singoleCitta[i] + ", " + singoliCAP[i]);
		}
			
		return indirizziCompleti;
	}
		
	public void addIndirizzo(String indirizzo, String citta, String cap) {
		this.indirizzi += "%" + indirizzo;
		this.citta += "%" + citta;
		this.cap += "%" + cap;
	}
	
	//Telephone
	
	public String getTelefono() {
		return this.telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono=telefono;
	}
	
	//email
	
	public String getEmail() {
		return this.email;
	}
	
	//Password
	
	public String getPw() {
		return this.pw.toString();
	}
	
	public void setPw(String pw) {
		this.pw=pw;
	}
		
}
