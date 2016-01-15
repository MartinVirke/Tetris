package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReadWriteHandler {

	public void readFile() {

	}

	public void saveFile(Object o) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("C:/temp/save.ser"))) {
			out.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GameLogic loadFile() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("C:/temp/save.ser"))) {
			return (GameLogic) in.readObject();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
		}
		return null;
	}

	public void writeFile() {

	}

}
