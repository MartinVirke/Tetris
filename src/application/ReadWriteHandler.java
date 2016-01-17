package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadWriteHandler {

	public void writeFile(Object o, String filename) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(setPath(filename)))) {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object readFiles(Object o, String filename) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(setPath(filename)))) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return o;
		}
	}

	private String setPath(String filename) {
		String pathString = null;
		File file = null;
		file = new File(filename);
		pathString = file.getPath();
		return pathString;
	}

}
