/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

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

    public APIException(Exception e) {
        super(e);
    }
}
