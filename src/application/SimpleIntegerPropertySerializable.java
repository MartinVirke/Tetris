package application;

import java.io.Serializable;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Holds the serializable variables for score and implements SimpleIntegerProperty so it can be listened to.
 * 
 * @author Martin Virke
 */

public class SimpleIntegerPropertySerializable extends SimpleIntegerProperty implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	private Integer serializableValue;

	/**
	 * @param initialValue
	 */

	public SimpleIntegerPropertySerializable(Integer initialValue) {
		super();
		serializableValue = initialValue;
	}

	/**
	 * @return serializableValue
	 */

	public Integer getSerializableValue() {
		return serializableValue;
	}

	/**
	 * Sets the value and calls the super.setValue() so any listener attached to
	 * that method will trigger.
	 * 
	 * @param value The new value.
	 */

	public void setSerializableValue(Integer value) {
		serializableValue = value;
		super.setValue(value); 
	}

}
