package predefinedValues;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;






import com.joestelmach.natty.*;
public class PredefinedValuesFactory {

	
	// empty string means no value can be obtained
	public static PredefinedValue getValue(String line, String type) {
		if(PredefinedValuesType.doesTypeExist(type)) {
			if(PredefinedValuesType.Email.equalsName(type)) {
				return getEmail(line);
			} else if(PredefinedValuesType.GPA.equalsName(type)) {
				return getGPA(line);
			} else if(PredefinedValuesType.Number.equalsName(type)) {
				return getNumber(line);
			} else if(PredefinedValuesType.Date.equalsName(type)) {
				return getDate(line);
			} else if(PredefinedValuesType.Duration.equalsName(type)) {
				return getDuration(line);
			} else if(PredefinedValuesType.EducationLevel.equalsName(type)) {
				return getEducationLevel(line);
			} // without info
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static PredefinedValue getGPA(String line) {
		Pattern p = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?/[0-9]*",
			    Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(line);
		Set<PredefinedValue> emails = new HashSet<PredefinedValue>();
		while(matcher.find()) {
			String[] target = matcher.group().split("/");
			return new SmartGPA(Double.parseDouble(target[0]), Double.parseDouble(target[1]));
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static PredefinedValue getNumber(String line) {
		Set<PredefinedValue> numbers = new HashSet<PredefinedValue>();
		
		char[] lineChars = line.toCharArray();
		String currentNumber= "";
		for(int i=0; i<lineChars.length; i++) {
			if((lineChars[i] >= '0' && lineChars[i] <='9') || lineChars[i] == ' '  || lineChars[i] == '+') {
				currentNumber += lineChars[i];
			} else  {
				currentNumber = "";
			}
		}
		
		if(currentNumber.trim().length() >= 8) {
			return new SmartNumber(currentNumber.trim());
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static PredefinedValue getEmail(String line) {
		Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b",
			    Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(line);
		Set<PredefinedValue> emails = new HashSet<PredefinedValue>();
		while(matcher.find()) {
			return new SmartEmail(matcher.group());
		}
		return null;
	}
	
	public static boolean containsStringIgnoreCases(String[] set, String target) {
		for(int i=0; i<set.length; i++) {
			if(target.toLowerCase().contains(set[i].toLowerCase())) {
				return true;
			}
		}
		return false;
		
	}
	@SuppressWarnings("unused")
	public static PredefinedValue getEducationLevel(String line) {
		Set<PredefinedValue> levels = new HashSet<PredefinedValue>();
		String[] phd = {"PhD"};
		String[] master ={"MSc", "Master","M.A", "M.S", "M.Ed.", "M.E.B", "M.Des", "M.N.C.M.", "M.S.N.", "M.S.W.",
				"M.P.A.", "M.P.C.", "M.P.P.", "M.P.H.", "M.C.", "M.C.A.", "M.Couns.", "M.L.A.", "M.L.I.S.", 
				"M.Div.", "A.L.M.", "M.M.", "M.B.A.", "M.Tech.", "M.I.T.B.", "M.B.E.", "M.Com.", "M.I.B.", "M.I.", "P.S.M."};
		
		String[] bachelor = {"Bachelor", "BE"};
		if(containsStringIgnoreCases(phd, line)) {
			return new SmartEducationLevel(SmartEducationLevel.phd);
			
		}
		
		if(containsStringIgnoreCases(master, line)) {
			return new SmartEducationLevel(SmartEducationLevel.master);
		}
		if(containsStringIgnoreCases(bachelor, line)) {
			return new SmartEducationLevel(SmartEducationLevel.bachelor);
			
		}
		return null;
		
	}
	
	@SuppressWarnings("unused")
	public static PredefinedValue getDate(String line) {
		Parser parser = new Parser();
		line.replaceAll("present", "now");
		line.replaceAll("Present", "now");
		List<DateGroup> groups = parser.parse(line.replaceAll("present", "now").replaceAll("Present", "now"));
		Set<PredefinedValue> dates = new HashSet<PredefinedValue>();
		for(DateGroup group:groups) {
			for(Date date:group.getDates()) {
				
				return new SmartDate(date);
			}

		}
		return null;
	}
	
	public static PredefinedValue getDuration(String line) {
		
		
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(line.replaceAll("present", "now").replaceAll("Present", "now"));
		List<Date> dates = new ArrayList<Date>();
		for(DateGroup group:groups) {
			for(Date date:group.getDates()) {
				dates.add( date);
//				Utils.debug(date.toString());
			}
		}
		if(dates.size() >=2 ) {
			return new SmartDuration(dates.get(0), dates.get(1));
		}
		return null;
	}

	public static PredefinedValue fromString(String stringValue, String type) {
		if(PredefinedValuesType.doesTypeExist(type)) {
			if(PredefinedValuesType.Email.equalsName(type)) {
				return SmartEmail.fromString(stringValue);
			} else if(PredefinedValuesType.GPA.equalsName(type)) {
				return SmartGPA.fromString(stringValue);
			} else if(PredefinedValuesType.Number.equalsName(type)) {
				
				return SmartNumber.fromString(stringValue);
			} else if(PredefinedValuesType.Date.equalsName(type)) {
				return SmartDate.fromString(stringValue);
			} else if(PredefinedValuesType.Duration.equalsName(type)) {
				return SmartDuration.fromString(stringValue);
			} else if(PredefinedValuesType.EducationLevel.equalsName(type)) {
				return SmartEducationLevel.fromString(stringValue);
			} // without info
		}
		return null;
		
	}

//	public static void main(String[] args) {
//		String toTestEducationLevel = "• M.Sc. (Integrated) in Physics (July 2010)      GPA: 7.3/10 ";
//		
////		Utils.debug(getEducationLevel(toTestEducationLevel).toString());
//		String toTestNumber = "National University of Singapore  Phone: +65 98632702 ";
////		Utils.debug(getNumber(toTestNumber).toString());
//		String toTestEmail = "Research Assistant                E-mail: deorani@gmail.com; elepd@nus.edu.sg";
//		
////		Utils.debug(getEmail(toTestEmail).toString());
////		Utils.debug(getDuration("• Research Scholar  Jan 2011 – present ").toString());
////		Utils.debug(getDate("now").toString());
////		getDuration(" Jan 2010 - now ");
//	}
}

