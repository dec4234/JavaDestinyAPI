/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * A connection could not be established to the Bungie servers. This is likely a problem with your internet or network
 * configuration.
 */
public class ConnectionException extends APIException {

    public ConnectionException(Exception exception) {
        super(exception);
    }
}
