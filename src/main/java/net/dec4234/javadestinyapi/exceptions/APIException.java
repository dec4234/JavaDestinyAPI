/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * Base class for all exceptions generated when an issue occurs in the request pipeline to Bungie. <br>
 * This is defined as a RuntimeException to give more flexibility internally (namely in {@link net.dec4234.javadestinyapi.material.DestinyAPI#searchUsers(String)}).
 * You should still try/catch this rather than deferring it to being a solely runtime exception
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
