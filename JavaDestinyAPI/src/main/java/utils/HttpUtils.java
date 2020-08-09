package utils;

import com.google.gson.*;
import exceptions.AccessTokenInvalidException;
import material.DestinyAPI;
import material.manifest.ManifestEntityTypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * A utils class used by JavaDestinyAPI to collect information from bungie.net
 * It is important that the DestinyAPI class is initialized prior to this class
 */
public class HttpUtils {

	String apiKey = DestinyAPI.getApiKey();
	private static String bearerToken = FileUtils.getInfo("access_token");

	public JsonObject urlRequestGET(String url) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										 .uri(URI.create(url))
										 .timeout(Duration.ofMinutes(1))
										 .header("X-API-KEY", apiKey)
										 .GET()
										 .build();
		CompletableFuture<String> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApplyAsync(HttpResponse::body);
		JsonElement parse = null;
		try {
			parse = new JsonParser().parse(response.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return parse.getAsJsonObject();
	}

	public JsonObject urlRequestGETOauth(String url) {
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

	/**
	 * Gets an access token using the refresh token in storage and replaces the old refresh token with the new one
	 * @return Returns the new access token
	 */
	public String setTokenViaRefresh() {
		String url = "https://www.bungie.net/Platform/App/OAuth/Token/";

		String requestBody = "grant_type=refresh_token&refresh_token=" + FileUtils.getInfo("refresh_token");

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
		FileUtils.setInfo("refresh_token", parse.getAsJsonObject().get("refresh_token").getAsString());
		FileUtils.setInfo("access_token", at);
		bearerToken = at;
		return parse.getAsJsonObject().get("access_token").getAsString();
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
		FileUtils.clear();
		FileUtils.setInfo("access_token", accessToken);
		FileUtils.setInfo("refresh_token", refreshToken);
		HttpUtils.bearerToken = FileUtils.getInfo("access_token");
	}

	public void checkFor401(String input) {
		if(input.contains("401 - Unauthorized")) {
			try {
				setTokenViaRefresh();
				throw new AccessTokenInvalidException("The access token used in this OAuth request was not accepted by the server \nI've already taken the liberty of getting a new access token for you :D");
			} catch (AccessTokenInvalidException e) {
				e.printStackTrace();
				System.exit(401);
			}
		}
	}
}
