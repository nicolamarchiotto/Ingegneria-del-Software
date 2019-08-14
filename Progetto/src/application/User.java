package application;

public class User implements Comparable<User>{

	private String nome;
	private String cognome;
	private String indirizzi;
	private String cap;
	private String citta;
	private String telefono;
	private String email;
	private String librocard;
	private String pw="";
	
	
	public User(String nome, String cognome, String indirizzi, String cap, String citta, String telefono, String email, String librocard,
			String pw) {
		this.nome=nome;
		this.cognome=cognome;
		this.indirizzi=indirizzi;
		this.cap=cap;
		this.citta=citta;
		this.telefono=telefono;
		this.email=email;
		this.librocard=librocard;
		this.pw=pw;
	}
	
	public User(String id, String pw) {
		this.email=id;
		this.pw=pw;
	}
	
	@Override
	public int compareTo(User other) {
		if((pw.equals(((User)other).pw)) && (email.equals(((User)other).email)) )
			return 0;
		else
			return -1;
				
	}
	
	public boolean equals(User other) {
		
			return true;
		else
			return false;
	}


}
