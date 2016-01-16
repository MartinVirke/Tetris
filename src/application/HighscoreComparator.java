package application;

import java.util.Comparator;

public class HighscoreComparator implements Comparator<HighscoreEntry> {

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
