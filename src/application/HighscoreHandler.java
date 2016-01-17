package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class for handling the creation, writing and sorting of the highscore, as
 * well as calculating if a score is high enough to make it on the list.
 * 
 * @author Martin Virke
 */

public class HighscoreHandler {

	private ArrayList<HighscoreEntry> highscore;
	private Comparator<HighscoreEntry> highscoreComparator;

	/**
	 * Creates a new ArrayList and Comparator, as well as adding a preset of
	 * names and scores to the list.
	 */

	public HighscoreHandler() {
		super();
		this.highscore = new ArrayList<>();
		this.highscoreComparator = new HighscoreComparator();

		// Aggregering: HighscoreHandler har (flera) HighscoreEntry, ett entry
		// kan dock leva med mening utan en instans av handler, så det är inte
		// en komposition. T.ex. sparas alla entries i en fil, och kan laddas in
		// i handlern igen senare.

		highscore.add(new HighscoreEntry("Olle", 2000));
		highscore.add(new HighscoreEntry("Jocke", 1000));
		highscore.add(new HighscoreEntry("Turbo", 750));
		highscore.add(new HighscoreEntry("Jonte", 500));
		highscore.add(new HighscoreEntry("Kalle", 100));
	}

	/**
	 * Adds an entry to the list, sorts the list and removes the 6th (lowest)
	 * entry.
	 * 
	 * @param name
	 *            Name of the new entrant.
	 * @param score
	 *            Score of the new entrant.
	 */

	public void addEntry(String name, int score) {
		highscore.add(new HighscoreEntry(name, score));
		sortList();
		if (highscore.size() > 5)
			highscore.remove(highscore.size() - 1);
	}

	private void sortList() {
		Collections.sort(highscore, highscoreComparator);
	}

	/**
	 * Checks if the score is high enough to be on the highscore list.
	 * 
	 * @param score
	 *            Score of the potential entrant.
	 * @return
	 */

	public boolean isHighscore(int score) {
		for (HighscoreEntry highscoreEntry : highscore) {
			if (score >= highscoreEntry.getScore()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the highscore list to a new highscore list, used when reading a
	 * highscore list from a file.
	 * 
	 * @param highscore
	 *            The new highscore list which is read from file.
	 */

	public void setHighscoreList(ArrayList<HighscoreEntry> highscore) {
		this.highscore = highscore;
	}

	/**
	 * Gets the highscore list for writing to file
	 * 
	 * @return
	 */

	public ArrayList<HighscoreEntry> getHighscoreList() {
		return highscore;
	}

	/**
	 * Compiles a String of all the entries for easy output.
	 * 
	 * @return returnString The compiled string of entries.
	 */

	public String getListString() {
		String returnString = "";
		for (int i = 0; i < highscore.size(); i++) {
			returnString += (i + 1) + ". " + highscore.get(i).toString() + System.lineSeparator();
		}
		return returnString;
	}

}
