/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * Indicates that the request returned error code 5  (The API has been disabled by Bungie)
 */
public class APIOfflineException extends APIException {

	public APIOfflineException(String returnMessage) {
		super("The Bungie API returned this message: " + returnMessage);
	}
}
