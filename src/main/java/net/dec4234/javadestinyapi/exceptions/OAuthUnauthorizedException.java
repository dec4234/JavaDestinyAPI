/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * You either have insufficient permission to attempt this OAuth action, or you forgot to authorize yourself.
 * See {@link net.dec4234.javadestinyapi.utils.framework.OAuthFlow}
 */
public class OAuthUnauthorizedException extends APIException {

    public OAuthUnauthorizedException(String message) {
        super(message);
    }
}
