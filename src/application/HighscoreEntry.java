package application;

import java.io.Serializable;
import java.util.Date;

public class HighscoreEntry implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;
	
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

	@Override
	public String toString() {
		return name + " " + score;
	}
}
