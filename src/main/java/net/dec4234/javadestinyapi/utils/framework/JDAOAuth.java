/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

/**
 * Contains all of the functions needed for the API to adequately store OAuth tokens.
 * <br>
 * Note: OAuth could allow potenially dangerous actions such as full control over your clan (if you are an admin) as
 * well as your inventory. Use at your own risk, and use good data management and protection practices.
 */
public interface JDAOAuth {

	String getAccessToken();
	String getRefreshToken();
	String getAPIToken();

	void setAccessToken(String accessToken);
	void setRefreshToken(String refreshToken);
	void setAPIToken(String apiToken);
}
