package predefinedValues;

public interface PredefinedValue extends Comparable<PredefinedValue> {

	public PredefinedValuesType getType();
	
	public String toString();
}
