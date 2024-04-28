/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
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
