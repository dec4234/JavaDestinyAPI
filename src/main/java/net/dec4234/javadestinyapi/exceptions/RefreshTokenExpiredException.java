/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.exceptions;

/**
 * The name is self-expanatory. The refresh token normally used to generate a new access token has expired since it
 * hasn't been refreshed in the past 90 days or it has been 1 year since the last oauth.
 * See {@link net.dec4234.javadestinyapi.utils.framework.OAuthFlow} to generate a new one.
 */
public class RefreshTokenExpiredException extends APIException {

}
