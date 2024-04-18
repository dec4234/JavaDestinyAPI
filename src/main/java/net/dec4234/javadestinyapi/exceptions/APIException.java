package net.dec4234.javadestinyapi.exceptions;

/**
 * Base class for all exceptions generated when an issue occurs in the request pipeline to Bungie.
 * This most common reason for this error would be when the API is offline, and all objects are misparsed.
 */
public abstract class APIException extends Exception {

    public APIException() {

    }

    public APIException(String errorMessage) {
        super(errorMessage);
    }
}
