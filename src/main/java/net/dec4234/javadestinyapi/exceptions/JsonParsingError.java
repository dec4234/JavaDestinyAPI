/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

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
