package predefinedValues;

import interpreter.Utils;

import java.util.ArrayList;

public enum PredefinedValuesType {
    Number ("Number"),
    Email ("Email"),
    GPA ("GPA"),
    Date ("Date"),
    Duration ("Duration"),
    EducationLevel ("EducationLevel"),
    Info("Info"),
    InvalidType("");

    private final String name;       

    private PredefinedValuesType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
    
    public static boolean doesTypeExist(String typeName) {
		for(PredefinedValuesType type: PredefinedValuesType.values()) {
			if(type.equalsName(typeName) && type != InvalidType) {
				return true;
			}
		}
		return false;
	}
    
    public static PredefinedValuesType fromString(String typeName) {
    	for(PredefinedValuesType type: PredefinedValuesType.values()) {
			if(type.equalsName(typeName) && type != InvalidType) {
				return type;
			}
		}
		return null;
    }
    
    
  
    
    public ArrayList<String> getTypeValues( ) {
    	ArrayList<String> output = new ArrayList<String>();
    	String[] nullArray = {"null"}; 
    	String[] array;
    	if(this == Duration) {
    		array = SmartDuration.getPossibleValues();
    	} else if (this == GPA) {
    		array = SmartGPA.getPossibleValues();
    	} else if(this == EducationLevel) {
    		array = SmartEducationLevel.getPossibleValues();
    	} else {
    		array = nullArray;
    	}
    	for(String v: array) {
    		output.add(v);
    	}
    	
    	return output;
    }
    
   
}