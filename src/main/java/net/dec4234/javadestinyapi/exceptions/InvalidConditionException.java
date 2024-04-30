/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * When a token is missing or wrong, or some other pre-condition was not met that was needed to execute a request.
 * This is a generic error, check the associated error message for more information.
 */
public class InvalidConditionException extends APIException {

    public InvalidConditionException(String message) {
        super(message);
    }
}
