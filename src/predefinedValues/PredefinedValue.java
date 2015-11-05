package predefinedValues;

import java.util.ArrayList;

public interface PredefinedValue extends Comparable<PredefinedValue> {

	public PredefinedValuesType getType();
	
	public String toString();
	
	public String[] getPossibleValues(); 
}
