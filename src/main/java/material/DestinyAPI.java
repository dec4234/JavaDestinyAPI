/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.clan.Clan;
import material.user.BungieUser;
import utils.HttpUtils;
import utils.framework.OAuthManager;

import java.util.ArrayList;
import java.util.List;

public class DestinyAPI {

	private static String apiKey = null;
	private static String clientId = null;
	private static String clientSecret = null;
	private static String oauthCode = null;
	private static String accessToken = null;
	private static String refreshToken = null;
	private static OAuthManager oam = null;
	private static boolean debugEnabled = false;

	public DestinyAPI setApiKey(String apiKey) {
		DestinyAPI.apiKey = apiKey;
		if(hasOauthManager()) {
			oam.setAPIToken(apiKey);
		}
		return this;
	}

	public DestinyAPI setClientID(String clientId) {
		DestinyAPI.clientId = clientId;
		return this;
	}

	public DestinyAPI setClientSecret(String clientSecret) {
		DestinyAPI.clientSecret = clientSecret;
		return this;
	}

	public DestinyAPI setOauthCode(String oauthCode) {
		DestinyAPI.oauthCode = oauthCode;
		return this;
	}

	public DestinyAPI setAccessToken(String accessToken) {
		DestinyAPI.accessToken = accessToken;
		if(hasOauthManager()) {
			oam.setAccessToken(accessToken);
		}
		return this;
	}

	public DestinyAPI setRefreshToken(String refreshToken) {
		DestinyAPI.refreshToken = refreshToken;
		if(hasOauthManager()) {
			oam.setRefreshToken(refreshToken);
		}
		return this;
	}

	public DestinyAPI enableDebugMode() {
		DestinyAPI.debugEnabled = true;

		return this;
	}

	public DestinyAPI disableDebugMode() {
		DestinyAPI.debugEnabled = false;

		return this;
	}

	/**
	 * Set the OAuth management class
	 * The class passed in this parameter must "extends OAuthManager"
	 */
	public DestinyAPI setOauthManager(OAuthManager oam) {
		DestinyAPI.oam = oam;
		// setApiKey(oam.getAPIToken());
		// setAccessToken(oam.getAccessToken());

		return this;
	}

	/**
	 * Return if the OauthManager class has been defined
	 */
	public static boolean hasOauthManager() {
		return oam != null;
	}


	public static BungieUser getUser(String id) { return new BungieUser(id); }

	/**
	 * Get a BungieUser from a Steam ID
	 * NOT the same IDs used by Bungie to identify individual users
	 */
	public static BungieUser getMemberFromSteamID(String steamID) {
		return getMemberFromPlatformID("SteamId", steamID);
	}

	private static BungieUser getMemberFromPlatformID(String platformName, String platformID) {
		JsonObject jsonObject = new HttpUtils().urlRequestGET("https://www.bungie.net/Platform/User/GetMembershipFromHardLinkedCredential/" + platformName + "/" + platformID + "/").getAsJsonObject("Response");

		return new BungieUser(jsonObject.get("membershipId").getAsString());
	}

	/**
	 * Gets the users with this name (There can be multiple users with the same name)
	 */
	public static List<BungieUser> getUsersWithName(String name) {
		HttpUtils hu = new HttpUtils();
		List<BungieUser> temp = new ArrayList<>();
		List<String> ids = new ArrayList<>();

		try {
			JsonObject obj = hu.urlRequestGET("https://www.bungie.net/platform/Destiny2/SearchDestinyPlayer/-1/" + name.replace(" ", "%20") + "/?components=204");
			JsonArray ja = obj.getAsJsonArray("Response");
			for (JsonElement je : ja) {
				JsonObject us = je.getAsJsonObject();
				BungieUser bu = new BungieUser(us.get("membershipId").getAsString());
				if (!ids.contains(bu.getBungieMembershipID())) {
					temp.add(bu);
					ids.add(bu.getBungieMembershipID());
				}
			}
		} catch (NullPointerException ignored) {

		}
		return temp;
	}

	/**
	 * Return a list of valid bungie users with that name
	 */
	public static List<BungieUser> getValidUsers(String name) {
		List<BungieUser> list = getUsersWithName(name);
		list.removeIf(bungieUser -> !bungieUser.isValidUser());
		return list;
	}

	public static Clan getClan(long id) {
		return new Clan(id);
	}

	public static Clan getClan(String name) {
		return new Clan(name);
	}

	public static String getApiKey() { return apiKey; }

	public static String getClientId() { return clientId; }

	public static String getClientSecret() { return clientSecret; }

	public static String getOauthCode() { return oauthCode; }

	public static String getAccessToken() {
		return oam.getAccessToken();
	}

	public static String getRefreshToken() {
		return oam.getRefreshToken();
	}

	public static boolean isDebugEnabled() { return DestinyAPI.debugEnabled; }

}
