import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

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
		officialRomanNumbers.entrySet()
		.stream()
		.map(entry -> 
		{
			String convertArabicToRomanNumber = convertArabicToRomanNumber(entry.getKey());	
			if (!convertArabicToRomanNumber.equals(entry.getValue()))
			{
				throw new RuntimeException(String.format("Invalid convertion of %1$d = %2$s, expecting: %3$s", entry.getKey(), convertArabicToRomanNumber, entry.getValue()));
			}	
			return new AbstractMap.SimpleEntry<Integer, String>(entry.getKey(), convertArabicToRomanNumber);
		})
		.forEach(System.out::println);
	}

	private static String convertArabicToRomanNumber(int i) {
		if (i <= 0) return "";
		if (exceptionalRomanNumbers.containsKey(i)) return exceptionalRomanNumbers.get(i);
		Optional<Integer> lowIndex = getLowBoundary(i);
		return exceptionalRomanNumbers.get(lowIndex.get()) + convertArabicToRomanNumber(i-lowIndex.get());
	}

	private static Optional<Integer> getLowBoundary(int i) {
		return exceptionalRomanNumbers.keySet().stream().filter(n -> n <= i).max(Integer::compare);
	}

	/**
	 * Loads the roman.properties file, contains [1,3999] roman numbers
	 */
	private static Map<Integer, String> loadProperties() throws Exception {
		Properties prop = new Properties();
		InputStream inputStream = RomanNumberConverter.class.getClassLoader().getResourceAsStream(ROMAN_PROPERTIES);

		if (inputStream != null) {
			prop.load(inputStream);
			
			return prop.entrySet()
					.stream()
					.sorted((e1, e2) -> new Integer((String)e1.getKey()).compareTo(new Integer((String)e2.getKey())))
					.collect(Collectors.toMap(entry -> new Integer((String)entry.getKey()), entry -> (String)entry.getValue()));
		} else {
			throw new FileNotFoundException("Property file '" + ROMAN_PROPERTIES + "' not found in the classpath");
		}
	}

}
