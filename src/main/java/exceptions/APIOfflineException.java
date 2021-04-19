/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package exceptions;

public class APIOfflineException extends Exception {

	public APIOfflineException() {
		super("The Bungie API is currently not responding. ");
	}
}
