/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
		if(temp.length() == 24) { // Sometimes the date will be in an extra precise format, which is truncated here
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

	/**
	 * Get the number of days since the time date provided
	 */
	public static double getDaysSinceTime(Date date) {
		DecimalFormat df = new DecimalFormat("0.##");
		return Double.parseDouble(df.format((new Date().getTime() - date.getTime()) / 1000.0 / 60.0 / 60.0 / 24.0));
	}

	/**
	 * Encode a string to be suitable for use in a url
	 *
	 * Specific characters need to be encoded in order to have a successful request
	 */
	public static String httpEncode(String input) {
		return input.replace(" ", "%20").replace("#", "%23").replace("^", "%5E");
	}

	public static void executeCommandLine(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
