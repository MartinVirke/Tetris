package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreHandler implements Serializable {

	private List<HighscoreEntry> highscore;
	private Comparator<HighscoreEntry> highscoreComparator;

	public HighscoreHandler() {
		super();
		this.highscore = new ArrayList<>();
		this.highscoreComparator = new HighscoreComparator();
	}

	public void addEntry(String name, int score) {
		highscore.add(new HighscoreEntry(name, score));
		sortList();
		if (highscore.size() > 5)
			highscore.remove(highscore.size() - 1);
	}

	private void sortList() {
		Collections.sort(highscore, highscoreComparator);
	}
	
	private boolean isHighscore(int score){
		for (HighscoreEntry highscoreEntry : highscore) {
			if(score >= highscoreEntry.getScore()){
				return true;
			}
		}
		return false;
	}

	public String getList() {
		String returnString = "";
		for (int i = 0; i < highscore.size(); i++) {
			returnString += (i + 1) + ". " + highscore.get(i).toString() + "\n";
		}
		return returnString;
	}

}
