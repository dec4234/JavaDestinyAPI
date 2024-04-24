/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.exceptions.APIOfflineException;
import net.dec4234.javadestinyapi.exceptions.ConnectionException;
import net.dec4234.javadestinyapi.exceptions.InvalidConditionException;
import net.dec4234.javadestinyapi.exceptions.JsonParsingError;
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

	private String apiKey = DestinyAPI.getApiKey();
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
		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.GET();
			return starter;
		})));
	}

	public JsonObject urlRequestGETOauth(String url) throws APIException {
		setTokenViaRefresh();
		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.GET()
				   .setHeader("Authorization", "Bearer " + HttpUtils.bearerToken);
			return starter;
		})));
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

		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.setHeader("Content-Type", "application/json")
				   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

			return starter;
		})));
	}

	public String urlRequestPOSTOauth(String url, String body) throws APIException {
		setTokenViaRefresh();
		if (body.isEmpty()) { body = "{\"message\": \"\",}"; }

		String finalBody = body;

		if(DestinyAPI.isDebugEnabled()) {
			System.out.println("Body: " + finalBody);
		}

		return getStringResponse(getRequest(true, url, starter -> {
			starter.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
				   .setHeader("Content-Type", "application/json")
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

		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
				   .setHeader("Content-Type", "application/json")
				   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

			return starter;
		})));
	}

	/**
	 * Make a request to the Bungie manifest to reveal information about a hash-identified item.
	 *
	 * Deprecated in favor of DestinyManifest#manifestGET()
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

		JsonObject response = getJsonObject(getStringResponse(getRequest(false, url, starter -> {
			starter.setHeader("Content-Type", "application/x-www-form-urlencoded")
				   .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
				   .POST(HttpRequest.BodyPublishers.ofString(requestBody));

			return starter;
		})));

		if(response.has("error_description") && response.get("error_description").getAsString().equals("ApplicationTokenKeyIdDoesNotExist")) {
			throw new InvalidConditionException("The refresh token is invalid, you likely need to generate new tokens");
		}

		if(!response.has("access_token")) {
			return null;
		}

		String at = response.get("access_token").getAsString();
		String rt = response.get("refresh_token").getAsString();
		bearerToken = at;
		new DestinyAPI().setAccessToken(at).setRefreshToken(rt);

		return at;
	}

	/**
	 * Requries an OAuthCode to be manually set inside of the DestinyAPI.setOAuthCode()
	 */
	public void setTokenViaAuth() throws APIException {
		setTokenViaAuth(DestinyAPI.getOauthCode());
	}

	public void setTokenViaAuth(String oAuthCode) throws APIException {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=authorization_code&code=" + oAuthCode;

		JsonObject jsonObject = getJsonObject(getStringResponse(getRequest(false, url, starter -> {
			starter.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
				   .setHeader("Content-Type", "application/x-www-form-urlencoded")
				   .POST(HttpRequest.BodyPublishers.ofString(requestBody));

			return starter;
		})));

		String accessToken = jsonObject.get("access_token").getAsString();
		String refreshToken = jsonObject.get("refresh_token").getAsString();

		new DestinyAPI().setAccessToken(accessToken).setRefreshToken(refreshToken);

		HttpUtils.bearerToken = accessToken;
	}

	private JsonObject getJsonObject(String stringResponse) throws APIException {
		JsonObject jsonObject;

		try {
			jsonObject = new JsonParser().parse(stringResponse).getAsJsonObject();
		} catch (JsonSyntaxException e) {
			throw new JsonParsingError(e);
		}

		// API Offline Check
		if(jsonObject.has("ErrorCode") && jsonObject.get("ErrorCode").getAsInt() == 5) {
			throw new APIOfflineException(jsonObject.get("Message").getAsString());
		}

		return jsonObject;
	}

	private String getStringResponse(HttpRequest httpRequest) throws ConnectionException {
		HttpClient httpClient = HttpClient.newHttpClient();
		try {
			String responseString = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body).get();

			if (DestinyAPI.isDebugEnabled()) {
				System.out.println(httpRequest.method() + " " + httpRequest.uri().toString());
				System.out.println(responseString);
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
