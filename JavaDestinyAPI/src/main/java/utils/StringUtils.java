package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils {

	/**
	 * @param zuluTimeInString The Zulu time, in the form of a String, you wish to convert
	 * @return Returns a date object representing the Zulu time provided
	 * @throws ParseException
	 */
	public static Date valueOfZTime(String zuluTimeInString) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("Zulu"));
		try {
			return df.parse(zuluTimeInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
