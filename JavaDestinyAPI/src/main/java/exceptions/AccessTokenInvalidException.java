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
