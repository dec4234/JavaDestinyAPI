/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarentees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Properties;

/**
 * A not so elegant solution to the problem of storing refresh tokens between uses
 * I'll change this to JSON at a later date
 */
public class FileUtils {

	private static Properties prop;
	private static OutputStream output;
	private static InputStream input;

	public static void checkFileStatus() {
		if(prop != null) return;
		try {
			prop = new Properties();
			input = new FileInputStream("" + FileSystems.getDefault().getPath(".") + "/config.properties");
			prop.load(input);
			output = new FileOutputStream("" + FileSystems.getDefault().getPath(".") + "/config.properties");
			save();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void setInfo(String key, String value) {
		checkFileStatus();
		if(prop.containsKey(key)) {
			prop.remove(key);
			save();
		}

		prop.setProperty(key, value);
		save();
	}

	public static String getInfo(String key) {
		checkFileStatus();
		return prop.getProperty(key);
	}

	public static boolean hasKey(String key) {
		checkFileStatus();
		return prop.getProperty(key) != null;
	}

	public static void clear() {
		checkFileStatus();
		prop.clear();
		save();
	}

	public static void save() {
		checkFileStatus();

		try {
			prop.store(output, null);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
