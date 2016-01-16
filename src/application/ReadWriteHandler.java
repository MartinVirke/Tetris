package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadWriteHandler {

	public void writeFile(Object o) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(setPath(o)))) {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object readFiles(Object o) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(setPath(o)))) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}

	private String setPath(Object o) {
		String pathString = null;
		File file = null;
		if (o.getClass().equals(GameLogic.class)) {
			file = new File("save.ser");
		} else if (o.getClass().equals(HighscoreHandler.class)){
			file = new File("hscore.ser");
		}
		pathString = file.getPath();
		System.out.println(pathString);
		return pathString;
	}

}
