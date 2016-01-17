package application;

import java.io.Serializable;
import java.util.Date;

/**
 * An entry to be placed into the highscore ArrayList. Stores the necessary
 * information for a meaningful entry. Serializable so the ArrayList can be
 * saved and loaded for later usage.
 * 
 * @author Martin Virke
 */

public class HighscoreEntry implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	private String name;
	private int score;
	private Date date;

	/**
	 * A new entry only needs a name and a score, the date is set automatically.
	 * 
	 * @param name Name of the entrant.
	 * @param score Score of the entrant.
	 */
	
	public HighscoreEntry(String name, int score) {
		super();
		this.name = name;
		this.score = score;
		this.date = new Date();
	}
	
	/**
	 * @return name Name of the entrant.
	 */

	public String getName() {
		return name;
	}

	/**
	 * @return score Score of the entrant.
	 */
	
	public int getScore() {
		return score;
	}

	/**
	 * @return date Date the entry was created.
	 */
	
	public Date getDate() {
		return date;
	}

	/**
	 * Overridden toString method for easier structure of highscore.
	 */
	
	@Override
	public String toString() {
		return name + " " + score;
	}
}
