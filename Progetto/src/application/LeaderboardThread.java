package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class LeaderboardUpdateThread extends Thread{  
	
	private Thread blinker;
	private final long WEEK = 600000; //attendo 10 minuti
	private long timePassed = 0;
	private LocalDateTime timeFromDB = null;
	
	public LeaderboardUpdateThread() {
		this.timeFromDB = this.getDBtime();
		this.timePassed = ChronoUnit.MINUTES.between(this.timeFromDB, LocalDateTime.now());
		System.out.println(this.timePassed);
		if(this.timePassed >= 10) {
			System.out.println("-----STO AGGIORNANDO LA CLASSIFICA: E' PASSATA UNA SETTIMANA-----"); 
			Classifica.updateClassifica(true);
			this.timeFromDB = this.timeFromDB.plusMinutes(this.timePassed / 10 * 10);
			this.updateDBtime(this.timeFromDB);
			this.timePassed = this.timePassed % 10;
		}
	}
	
	public void run(){
		blinker = (Thread)this;
		while(blinker == (Thread)this) {
			try {
				sleep(WEEK - this.timePassed * 60000); 
				this.timePassed = 0;
				System.out.println("-----STO AGGIORNANDO LA CLASSIFICA: E' PASSATA UNA SETTIMANA-----"); 
				Classifica.updateClassifica(true);
				this.timeFromDB = this.timeFromDB.plusMinutes(10);
				this.updateDBtime(this.timeFromDB);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	public void interrupt() {
		blinker = null;
	}
	
	//prendo la data di riferimento dal database
	private LocalDateTime getDBtime() {
		Statement stmt = null;
		
		try {
			stmt = SqliteConnection.dbConnector().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM DateList WHERE id = 'Week'");
			if(rs.next())
				return LocalDateTime.of(rs.getInt("anno"), rs.getInt("mese"), rs.getInt("giorno"), rs.getInt("ora"), rs.getInt("minuto"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	//aggiorno la data di riferimento del database
	private LocalDateTime updateDBtime(LocalDateTime date) {
		Statement stmt = null;
		
		try {
			stmt = SqliteConnection.dbConnector().createStatement();
			stmt.executeUpdate("UPDATE DateList SET\ngiorno = " + date.getDayOfMonth() + ",\nmese = " + date.getMonthValue() + ",\nanno = " + date.getYear() + ",\nora = " + date.getHour() + ",\nminuto = " + date.getMinute() + "\nWHERE id = 'Week'");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;	
	}
} 
