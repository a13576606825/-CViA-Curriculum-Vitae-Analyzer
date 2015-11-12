package predefinedValues;

import evaluator.Comparator;

public interface PredefinedValue extends Comparable<PredefinedValue> {

	public PredefinedValuesType getType();
	
	public String toString();
	
	public boolean compare(Comparator comparator, String toCompare);
}
