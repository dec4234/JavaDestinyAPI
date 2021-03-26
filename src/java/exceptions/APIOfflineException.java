package exceptions;

public class APIOfflineException extends Exception {

	public APIOfflineException() {
		super("The Bungie API is currently not responding. ");
	}
}
