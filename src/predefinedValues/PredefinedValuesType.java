package predefinedValues;

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
}