/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarentees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package exceptions;

/**
 * Thrown whenever a 401 error is detected in the HTTP response
 * This error indicates that the OAuth access token used by the request was not accepted by the server
 */
public class AccessTokenInvalidException extends Exception {

	public AccessTokenInvalidException() {
	}

	public AccessTokenInvalidException(String message) {
		super(message);
	}
}
