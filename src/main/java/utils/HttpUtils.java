/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils;

import com.google.gson.*;
import exceptions.APIOfflineException;
import exceptions.AccessTokenInvalidException;
import material.DestinyAPI;
import material.manifest.ManifestEntityTypes;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A utils class used by JavaDestinyAPI to collect information from bungie.net
 * It is important that the DestinyAPI class is initialized prior to this class
 */
public class HttpUtils {

	String apiKey = DestinyAPI.getApiKey();
	private static String bearerToken;

	/**
	 * Send a GET url request to the url provided, returns a JsonObject of the response
	 */
	public JsonObject urlRequestGET(String url) {
		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.GET();
			return starter;
		})));
	}

	public JsonObject urlRequestGETOauth(String url) {
		setTokenViaRefresh();
		return getJsonObject(getStringResponse(getRequest(true, url, starter -> {
			starter.GET()
				   .setHeader("Authorization", "Bearer " + HttpUtils.bearerToken);
			return starter;
		})));
	}

	public String urlRequestPOST(String url, String body) {
		if (body.isEmpty()) { body = "{\"message\": \"\",}"; }
		String finalBody = body;
		return getStringResponse(getRequest(true, url, starter -> {
			starter.setHeader("Content-Type", "application/json")
				   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

			return starter;
		}));
	}

	public String urlRequestPOSTOauth(String url, String body) {
		setTokenViaRefresh();
		if (body.isEmpty()) { body = "{\"message\": \"\",}"; }

		String finalBody = body;
		return getStringResponse(getRequest(true, url, starter -> {
			starter.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
				   .setHeader("Content-Type", "application/json")
				   .POST(HttpRequest.BodyPublishers.ofString(finalBody));

			return starter;
		}));
	}

	/**
	 * Make a request to the Bungie manifest to reveal information about a hash-identified item
	 */
	public JsonObject manifestGET(ManifestEntityTypes entityType, String hashIdentifier) {
		return urlRequestGET("https://www.bungie.net/Platform/Destiny2/Manifest/" + entityType.getBungieEntityValue() + "/" + hashIdentifier + "/");
	}

	/**
	 * Gets an access token using the refresh token in storage and replaces the old refresh token with the new one
	 *
	 * @return Returns the new access token
	 */
	public String setTokenViaRefresh() {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=refresh_token&refresh_token=" + DestinyAPI.getRefreshToken();

		JsonObject response = getJsonObject(getStringResponse(getRequest(false, url, starter -> {
			starter.setHeader("Content-Type", "application/x-www-form-urlencoded")
				   .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
				   .POST(HttpRequest.BodyPublishers.ofString(requestBody));

			return starter;
		})));

		String at = response.get("access_token").getAsString();
		String rt = response.get("refresh_token").getAsString();
		bearerToken = at;
		new DestinyAPI().setAccessToken(at).setRefreshToken(rt);

		return at;
	}

	public void setTokenViaAuth() {
		setTokenViaAuth(DestinyAPI.getOauthCode());
	}

	public void setTokenViaAuth(String oAuthCode) {
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

	public boolean checkFor401(String input) {
		if (input.contains("401 - Unauthorized")) {
			try {
				setTokenViaRefresh();
				throw new AccessTokenInvalidException("The access token used in this OAuth request was not accepted by the server \nI've already taken the liberty of getting a new access token for you :D");
			} catch (AccessTokenInvalidException e) {
				e.printStackTrace();
				return true;
			}
		}

		return false;
	}

	private JsonObject getJsonObject(String stringResponse) {
		JsonObject jsonObject = new JsonParser().parse(stringResponse).getAsJsonObject();

		// API Offline Check
		if(jsonObject.has("ErrorCode") && jsonObject.get("ErrorCode").getAsInt() == 5) {
			try {
				throw new APIOfflineException(jsonObject.get("Message").getAsString());
			} catch (APIOfflineException exception) {
				exception.printStackTrace();
			}
		}
		return jsonObject;
	}

	private String getStringResponse(HttpRequest httpRequest) {
		HttpClient httpClient = HttpClient.newHttpClient();
		try {
			String responseString = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body).get();

			if (DestinyAPI.isDebugEnabled()) {
				System.out.println(responseString);
			}
			return responseString;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return null;
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
