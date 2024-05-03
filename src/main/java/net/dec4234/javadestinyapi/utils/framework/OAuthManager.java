/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

/**
 * Used to direct the storage of OAuth tokens using the user's own code. See {@link OAuthFlow} and {@link JDAOAuth} for
 * more info.
 * <br>
 * Note: OAuth could allow potentially dangerous actions such as full control over your clan (if you are an admin) as
 * well as your inventory. Use at your own risk, and use good data management and protection practices.
 * <br>
 * See {@link JsonOAuthManager} for an example implementation
 */
public abstract class OAuthManager implements JDAOAuth {
}
