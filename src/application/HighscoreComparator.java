package application;

import java.util.Comparator;

/**
 * A comparator for sorting the highscore in a meaningful order.
 * 
 * @author Martin Virke
 */

public class HighscoreComparator implements Comparator<HighscoreEntry> {

	/**
	 * Sorts highscore entries so the one with the highest score is placed
	 * first. Should two entries have the same score a Date variable will decide
	 * who goes ahead of the other. Newer highscores are placed above older
	 * ones.
	 */

	@Override
	public int compare(HighscoreEntry he1, HighscoreEntry he2) {
		if (he1.getScore() < he2.getScore()) {
			return 1;
		} else if (he1.getScore() == he2.getScore()) {
			if (he1.getDate().getTime() < he2.getDate().getTime()) {
				return 1;
			} else if (he1.getDate().getTime() > he2.getDate().getTime()) {
				return -1;
			} else
				return 0;
		} else
			return -1;
	}
}
