/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarentees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

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
		String temp = zuluTimeInString;
		if(temp.length() == 24) {
			temp = temp.substring(0, temp.length() - 5);
			temp = temp + "Z";
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("Zulu"));
		try {
			return df.parse(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
