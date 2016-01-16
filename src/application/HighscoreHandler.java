package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighscoreHandler {

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

}
