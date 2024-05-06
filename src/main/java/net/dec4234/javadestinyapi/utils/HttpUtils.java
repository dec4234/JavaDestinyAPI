/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.exceptions.APIOfflineException;
import net.dec4234.javadestinyapi.exceptions.AccessTokenExpiredException;
import net.dec4234.javadestinyapi.exceptions.ConnectionException;
import net.dec4234.javadestinyapi.exceptions.JsonParsingError;
import net.dec4234.javadestinyapi.exceptions.OAuthUnauthorizedException;
import net.dec4234.javadestinyapi.exceptions.RefreshTokenExpiredException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

/**
 * A net.dec4234.javadestinyapi.utils class used by JavaDestinyAPI to collect information from bungie.net
 * It is important that the DestinyAPI class is initialized prior to this class
 */
public class HttpUtils {

	public static final String URL_BASE = "https://www.bungie.net/Platform";

	private String apiKey;
	private static String bearerToken;

	public HttpUtils(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Send a GET url request to the url provided, returns a JsonObject of the response
	 */
	public JsonObject urlRequestGET(String url) throws APIException {
		return getJsonObject(getRequest(true, url, starter -> {
			starter.GET();
			return starter;
		}));
	}

	public JsonObject urlRequestGETOauth(String url) throws APIException {
		try {
			return getJsonObject(getRequest(true, url, starter -> {
				starter.GET()
					   .setHeader("Authorization", "Bearer " + HttpUtils.bearerToken);
				return starter;
			}));
		} catch (AccessTokenExpiredException e) {
			setTokenViaRefresh();

			return getJsonObject(getRequest(true, url, starter -> {
				starter.GET()
					   .setHeader("Authorization", "Bearer " + HttpUtils.bearerToken);
				return starter;
			}));
		}
	}

	public JsonObject urlRequestPOST(String url, JsonObject body) throws APIException {
		return urlRequestPOST(url, body.toString());
	}

	public JsonObject urlRequestPOST(String url, String body) throws APIException {
		if (body.isEmpty()) { body = "{\"message\": \"\",}"; }
		String finalBody = body;

		if(DestinyAPI.isDebugEnabled()) {
			System.out.println("Body: " + finalBody);
		}

		return getJsonObject(getRequest(true, url, starter -> {
			starter.setHeader("Content-Type", "application/json")
				   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

			return starter;
		}));
	}

	public JsonObject urlRequestPOSTOauth(String url, JsonObject body) throws APIException {
		setTokenViaRefresh();
		if (body.toString().isEmpty()) {
			body = new JsonObject();
			body.addProperty("message", "");
		}

		final String finalBody = body.toString();

		if(DestinyAPI.isDebugEnabled()) {
			System.out.println("Body: " + finalBody);
		}

		try {
			return getJsonObject(getRequest(true, url, starter -> {
				starter.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
					   .setHeader("Content-Type", "application/json")
					   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

				return starter;
			}));
		} catch (AccessTokenExpiredException e) {
			setTokenViaRefresh();
			return getJsonObject(getRequest(true, url, starter -> {
				starter.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
					   .setHeader("Content-Type", "application/json")
					   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

				return starter;
			}));
		}
	}

	public JsonObject urlRequestPOSTOauth(String url) throws APIException {
		return urlRequestPOSTOauth(url, new JsonObject());
	}

	/**
	 * Make a request to the Bungie manifest to reveal information about a hash-identified item.
	 *
	 * Deprecated in favor of {@link net.dec4234.javadestinyapi.material.manifest.DestinyManifest#manifestGET(ManifestEntityTypes, String)}
	 */
	@Deprecated
	public JsonObject manifestGET(ManifestEntityTypes entityType, String hashIdentifier) throws APIException {
		return urlRequestGET("https://www.bungie.net/Platform/Destiny2/Manifest/" + entityType.getBungieEntityValue() + "/" + hashIdentifier + "/");
	}

	/**
	 * Gets an access token using the refresh token in storage and replaces the old refresh token with the new one
	 *
	 * @return Returns the new access token
	 */
	public String setTokenViaRefresh() throws APIException {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=refresh_token&refresh_token=" + DestinyAPI.getRefreshToken();

		JsonObject response = getJsonObject(getRequest(false, url, starter -> {
			starter.setHeader("Content-Type", "application/x-www-form-urlencoded")
				   .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
				   .POST(HttpRequest.BodyPublishers.ofString(requestBody));

			return starter;
		}));

		if(response.has("error_description") && response.get("error_description").getAsString().equals("ApplicationTokenKeyIdDoesNotExist")) {
			throw new RefreshTokenExpiredException();
		}

		if(!response.has("access_token")) {
			return null;
		}

		String at = response.get("access_token").getAsString();
		String rt = response.get("refresh_token").getAsString();
		bearerToken = at;
		DestinyAPI.setAccessToken(at);
		DestinyAPI.setRefreshToken(rt);

		if(DestinyAPI.isDebugEnabled()) {
			System.out.println("TOKENS REFRESHED");
		}

		return at;
	}

	/**
	 * Requries an OAuthCode to be manually set inside the DestinyAPI.setOAuthCode()
	 */
	public void setTokenViaAuth() throws APIException {
		setTokenViaAuth(DestinyAPI.getOauthCode());
	}

	public void setTokenViaAuth(String oAuthCode) throws APIException {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=authorization_code&code=" + oAuthCode;

		JsonObject jsonObject = getJsonObject(getRequest(false, url, starter -> {
			starter.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
				   .setHeader("Content-Type", "application/x-www-form-urlencoded")
				   .POST(HttpRequest.BodyPublishers.ofString(requestBody));

			return starter;
		}));

		String accessToken = jsonObject.get("access_token").getAsString();
		String refreshToken = jsonObject.get("refresh_token").getAsString();

		DestinyAPI.setRefreshToken(refreshToken);
		DestinyAPI.setAccessToken(accessToken);

		HttpUtils.bearerToken = accessToken;
	}

	public boolean hasValidOAuthTokens() throws APIException {
		boolean value = DestinyAPI.hasOauthManager() && DestinyAPI.getAccessToken() != null && DestinyAPI.getHttpUtils().setTokenViaRefresh() != null;

		if(value) {
			try {
				DestinyAPI.getHttpUtils().setTokenViaRefresh();
			} catch (AccessTokenExpiredException | RefreshTokenExpiredException | OAuthUnauthorizedException e) {
				value = false;
			}
		}

		return value;
	}

	private JsonObject getJsonObject(HttpRequest httpRequest) throws APIException {
		HttpClient httpClient = HttpClient.newHttpClient();
		String responseString;

		try { // TODO: are we even taking advantage of async? this seems pointless to just block right away
			responseString = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body).get();

			if (DestinyAPI.isDebugEnabled()) {
				System.out.println(httpRequest.method() + " " + httpRequest.uri().toString());
				System.out.println(responseString);
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new ConnectionException(e);
		}

		JsonObject jsonObject;

		try {
			jsonObject = JsonParser.parseString(responseString).getAsJsonObject();
		} catch (JsonSyntaxException e) {
			throw new JsonParsingError(e);
		}

		// Check for API errors - https://bungie-net.github.io/multi/schema_Exceptions-PlatformErrorCodes.html#schema_Exceptions-PlatformErrorCodes
		if(jsonObject.has("ErrorCode")) {
			switch (jsonObject.get("ErrorCode").getAsInt()) { //TODO: lots of errors we could catch here
				case 5: // APIOffline
					throw new APIOfflineException(jsonObject.get("Message").getAsString());
				case 99: // WebAuthRequired
					throw new OAuthUnauthorizedException("OAuth - access denied. Try authenticating.");
				case 2111 | 2115: // AccessTokenHasExpired, OAuthAccessTokenExpired
					throw new AccessTokenExpiredException();
				case 2118: // RefreshTokenExpired -- need to reauth using oauth
					throw new RefreshTokenExpiredException();
			}
		}

		return jsonObject;
	}

	private String getStringResponse(HttpRequest httpRequest) throws ConnectionException {
		HttpClient httpClient = HttpClient.newHttpClient();
		try {
			String responseString = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body).get();

			if (DestinyAPI.isDebugEnabled()) {
				System.out.println(httpRequest.method() + " " + httpRequest.uri().toString());
				System.out.println("Response: " + responseString);
			}
			return responseString;
		} catch (InterruptedException | ExecutionException e) {
			throw new ConnectionException(e);
		}
	}

	private HttpRequest getRequest(boolean standardRequest, String url, HttpRequestModifier httpRequestModifier) {
		HttpRequest.Builder builder = httpRequestModifier.modifyRequest(HttpRequest.newBuilder());

		builder.uri(URI.create(url)).timeout(Duration.ofMinutes(3));

		if(standardRequest) {
			builder.setHeader("X-API-KEY", apiKey);
		}

		return builder.build();
	}
}
