package application;



class LeaderboardUpdateThread extends Thread{  
	
	private Thread blinker;
	private final long WEEK = 600000; //attendo 10 minuti
	
	public void run(){
		blinker = (Thread)this;
		while(blinker == (Thread)this) {
			try {
				sleep(WEEK); 
				System.out.println("-----STO AGGIORNANDO LA CLASSIFICA: E' PASSATA UNA SETTIMANA-----"); 
				Classifica.updateClassifica(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	public void interrupt() {
		blinker = null;
	}
} 
