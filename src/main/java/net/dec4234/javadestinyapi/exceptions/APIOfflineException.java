/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * Indicates that the request returned error code 5  (The API has been disabled by Bungie)
 */
public class APIOfflineException extends Exception {

	public APIOfflineException(String returnMessage) {
		super("The Bungie API returned this message: " + returnMessage);
	}
}
