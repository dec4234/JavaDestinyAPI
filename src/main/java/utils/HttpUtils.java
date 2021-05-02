/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils;

import com.google.gson.*;
import exceptions.AccessTokenInvalidException;
import material.DestinyAPI;
import material.manifest.ManifestEntityTypes;

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
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(2))
										 .header("X-API-KEY", apiKey)
										 .GET()
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		JsonElement parse = null;
		try {
			parse = new JsonParser().parse(response.get()); // Parse response to JSON
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return null;
		}
		return parse.getAsJsonObject();
	}

	public Object urlRequestGETstring(String url) throws ExecutionException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .header("X-API-KEY", apiKey)
										 .GET()
										 .build();
		HttpResponse<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get();
		return response;
	}

	public JsonObject urlRequestGETOauth(String url) {
		setTokenViaRefresh();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(1))
				.setHeader("X-API-KEY", apiKey)
				.setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
				.GET()
				.build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		JsonElement parse = null;
		try {
			checkFor401(response.get());
			parse = new JsonParser().parse(response.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return parse.getAsJsonObject();
	}

	public String urlRequestPOST(String url, String body) {
		if(body.isEmpty()) body = "{\"message\": \"\",}";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .setHeader("X-API-KEY", apiKey)
										 .setHeader("Content-Type", "application/json")
										 .POST(HttpRequest.BodyPublishers.ofString(body))
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		try {
			return response.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String urlRequestPOSTOauth(String url, String body) {
		setTokenViaRefresh();

		if(body.isEmpty()) body = "{\"message\": \"\",}";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .setHeader("X-API-KEY", apiKey)
										 .setHeader("Authorization", "Bearer " + HttpUtils.bearerToken)
										 .setHeader("Content-Type", "application/json")
										 .POST(HttpRequest.BodyPublishers.ofString(body))
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		try {
			checkFor401(response.get());
			return response.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JsonObject manifestGET(ManifestEntityTypes entityType, String hashIdentifier) {
		return urlRequestGET("https://www.bungie.net/Platform/Destiny2/Manifest/" + entityType.getBungieEntityValue() + "/" + hashIdentifier + "/");
	}

	public String generateLineGraph() {
		String body = "{\"chart\": {\"type\": \"line\", \"data\": {\"labels\": [\"Hello\", \"World\"], \"datasets\": [{\"label\": \"Foo\", \"data\": [1, 2]}]}}}";
		return urlRequestPOST("https://quickchart.io/chart/create", body);
	}

	/**
	 * Gets an access token using the refresh token in storage and replaces the old refresh token with the new one
	 * @return Returns the new access token
	 */
	public String setTokenViaRefresh() {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=refresh_token&refresh_token=" + DestinyAPI.getRefreshToken();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .setHeader("Content-Type", "application/x-www-form-urlencoded")
										 .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
										 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		JsonElement parse = null;
		try {
			parse = new JsonParser().parse(response.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		String at = parse.getAsJsonObject().get("access_token").getAsString();
		String rt = parse.getAsJsonObject().get("refresh_token").getAsString();
		bearerToken = at;
		new DestinyAPI().setAccessToken(at).setRefreshToken(rt);

		return at;
	}

	public void setTokenViaAuth() {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=authorization_code&code=" + DestinyAPI.getOauthCode();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((DestinyAPI.getClientId() + ":" + DestinyAPI.getClientSecret()).getBytes()))
										 .setHeader("Content-Type", "application/x-www-form-urlencoded")
										 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		JsonElement parse = null;
		try {
			parse = new JsonParser().parse(response.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		String accessToken = parse.getAsJsonObject().get("access_token").getAsString();
		String refreshToken = parse.getAsJsonObject().get("refresh_token").getAsString();

		new DestinyAPI().setAccessToken(accessToken).setRefreshToken(refreshToken);

		HttpUtils.bearerToken = accessToken;
	}

	public boolean checkFor401(String input) {
		if(input.contains("401 - Unauthorized")) {
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
}
