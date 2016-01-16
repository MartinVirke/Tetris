package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighscoreHandler {

	private ArrayList<HighscoreEntry> highscore;
	private Comparator<HighscoreEntry> highscoreComparator;

	public HighscoreHandler() {
		super();
		this.highscore = new ArrayList<>();
		this.highscoreComparator = new HighscoreComparator();
		highscore.add(new HighscoreEntry("Olle", 2000));
		highscore.add(new HighscoreEntry("Jocke", 1000));
		highscore.add(new HighscoreEntry("Turbo", 750));
		highscore.add(new HighscoreEntry("Jonte", 500));
		highscore.add(new HighscoreEntry("Kalle", 100));
	}

	public void addEntry(String name, int score) {
		highscore.add(new HighscoreEntry(name, score));
		sortList();
		System.out.println("added");
		if (highscore.size() > 5)
			highscore.remove(highscore.size() - 1);
	}

	private void sortList() {
		Collections.sort(highscore, highscoreComparator);
	}

	public boolean isHighscore(int score) {
		for (HighscoreEntry highscoreEntry : highscore) {
			if (score >= highscoreEntry.getScore()) {
				return true;
			}
		}
		return false;
	}
	
	public void setHighscoreList(ArrayList<HighscoreEntry> highscore){
		this.highscore = highscore;
	}
	
	public ArrayList<HighscoreEntry> getHighscoreList(){
		return highscore;
	}

	public String getListString() {
		String returnString = "";
		for (int i = 0; i < highscore.size(); i++) {
			returnString += (i + 1) + ". " + highscore.get(i).toString() + System.lineSeparator();
		}
		return returnString;
	}

}
