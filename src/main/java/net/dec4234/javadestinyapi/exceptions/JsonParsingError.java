package net.dec4234.javadestinyapi.exceptions;

import com.google.gson.JsonSyntaxException;

/**
 * Malformed or non-json content found where JSON was expected. Usually at an API endpoint.
 */
public class JsonParsingError extends APIException {

    public JsonParsingError(String message) {
        super(message);
    }

    public JsonParsingError(JsonSyntaxException exception) {
        super(exception);
    }
}
