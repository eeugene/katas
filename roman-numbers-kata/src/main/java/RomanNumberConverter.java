import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.Maps;


public class RomanNumberConverter {

	private static final String ROMAN_PROPERTIES = "roman.properties";
	static Map<Integer,String> exceptionalRomanNumbers = Maps.newTreeMap();
	
	public static void main(String[] args) throws Exception {
		
		// list of exceptions
		exceptionalRomanNumbers.put(1, "I");
		exceptionalRomanNumbers.put(4, "IV");
		exceptionalRomanNumbers.put(5, "V");
		exceptionalRomanNumbers.put(9, "IX");
		exceptionalRomanNumbers.put(10, "X");
		exceptionalRomanNumbers.put(40, "XL");
		exceptionalRomanNumbers.put(50, "L");
		exceptionalRomanNumbers.put(90, "XC");
		exceptionalRomanNumbers.put(100, "C");
		exceptionalRomanNumbers.put(400, "CD");
		exceptionalRomanNumbers.put(500, "D");
		exceptionalRomanNumbers.put(900, "CM");
		exceptionalRomanNumbers.put(1000, "M");
		
		// load roman referential, to check values
		Map<Integer, String> officialRomanNumbers = loadProperties();
		
		// convert, check and display all values [1,3999]
		for (Entry<Integer, String> expectingValue : officialRomanNumbers.entrySet()) 
		{
			String romanNumber = convertArabicToRomanNumber(expectingValue.getKey());
			if (!romanNumber.equals(expectingValue.getValue()))
			{
				throw new RuntimeException(String.format("Invalid convertion of %1$d = %2$s, expecting: %3$s", expectingValue.getKey(), romanNumber, expectingValue.getValue()));
			}
			System.out.println(expectingValue.getKey() + " = "+ romanNumber);
		}
	}

	private static String convertArabicToRomanNumber(int i) {
		if (i <= 0) return "";
		if (exceptionalRomanNumbers.containsKey(i)) return exceptionalRomanNumbers.get(i);
		Entry<Integer, String> low = getLowBoundary(i);
		return low.getValue() + convertArabicToRomanNumber(i-low.getKey());
	}

	private static Entry<Integer, String> getLowBoundary(int i) {
		Entry<Integer, String> find = null;
		for (Entry<Integer, String> entry : exceptionalRomanNumbers.entrySet()) {
			if (entry.getKey() > i) break; // found
			find = entry;
		}
		return find;
	}

	/**
	 * Loads the roman.properties file, contains [1,3999] roman numbers
	 */
	private static TreeMap<Integer, String> loadProperties() throws Exception {
		Properties prop = new Properties();
		InputStream inputStream = RomanNumberConverter.class.getClassLoader().getResourceAsStream(ROMAN_PROPERTIES);
		TreeMap<Integer, String> newTreeMap = Maps.newTreeMap();

		if (inputStream != null) {
			prop.load(inputStream);
			Set<Entry<Object, Object>> entrySet = prop.entrySet();
			for (Entry<Object, Object> entry : entrySet) {
				newTreeMap.put(Integer.valueOf((String)entry.getKey()), (String)entry.getValue());
			}
			return newTreeMap;
		} else {
			throw new FileNotFoundException("Property file '" + ROMAN_PROPERTIES + "' not found in the classpath");
		}
	}

}
