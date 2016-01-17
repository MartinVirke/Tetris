package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for handling all reading and writing to file.
 * 
 * @author Martin Virke
 */

public class ReadWriteHandler {

	/**
	 * Writes to the specified file. Throws IOException which is caught higher
	 * up and forces a display of the error on screen, alerting the user that
	 * the application was unable to write.
	 * 
	 * @param o
	 *            The object to write.
	 * @param filename
	 *            The filename to write to.
	 * @throws IOException
	 *             Exception to be caught and handled higher up.
	 */

	public void writeFile(Object o, String filename) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(setPath(filename)))) {
			out.writeObject(o);
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Reads the specified file.
	 * 
	 * @param o
	 *            The object to return if read fails.
	 * @param filename
	 *            Name of file to read.
	 * @return
	 */

	public Object readFiles(Object o, String filename) {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(setPath(filename)))) {
			return in.readObject();
		} catch (IOException | ClassNotFoundException e) {
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
