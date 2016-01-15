package application;

import java.util.Date;

public class HighscoreEntry {

	private String name;
	private int score;
	private Date date;
	
	public HighscoreEntry(String name, int score) {
		super();
		this.name = name;
		this.score = score;
		this.date = new Date();
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public Date getDate() {
		return date;
	}
	
}
