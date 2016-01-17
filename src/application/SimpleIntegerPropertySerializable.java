package application;

import java.io.Serializable;

import javafx.beans.property.SimpleIntegerProperty;

// This class holds the serializable variables for score.

public class SimpleIntegerPropertySerializable extends SimpleIntegerProperty implements Serializable {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;
	
	private Integer serializableValue;
	
	public SimpleIntegerPropertySerializable(Integer initialValue) {
		super();
		serializableValue = initialValue;
	}
	
	public Integer getSerializableValue(){
		return serializableValue;
	}
	
	public void setSerializableValue(Integer value){
		serializableValue = value;
		super.setValue(value); //to trigger the listener for writing to scoreLabel.
	}
	
}
