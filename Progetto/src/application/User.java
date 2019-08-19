package application;

import java.util.List;

public class User implements Comparable<Object>{

	private String nome;
	private String cognome;
	private String indirizzi;
	private String cap;
	private String citta;
	private String telefono;
	private String email;
	private String librocard;
	private String pw;
	
	
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
		
		//implementazione Librocard con data, da programmare con un libreria time o rebe lol del genere0
		//this.librocard=new LibroCard(// )
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
	
	public String getEmail() {
		return this.email;
	}
	
	//verify->false = email già utilizzata, usane un'altra
	public boolean verifyId(List<User> l) {
		for(User u: l) {
			if(this.email.equals(u.getEmail()))
				return false;
			else
				return true;
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
		return this.email + " " +  this.pw;
	}

}
