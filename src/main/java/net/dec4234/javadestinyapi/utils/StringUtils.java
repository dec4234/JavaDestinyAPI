/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
		} catch (ParseException e) { // TODO: log error here
			return null;
		}
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
		return URLEncoder.encode(input, StandardCharsets.UTF_8);
	}

	public static void executeCommandLine(String command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
