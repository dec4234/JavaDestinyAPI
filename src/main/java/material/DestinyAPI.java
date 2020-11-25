package material;

import com.google.gson.*;
import material.clan.Clan;
import material.user.BungieUser;
import utils.HttpUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DestinyAPI {

	private static String apiKey = null;
	private static String clientId = null;
	private static String clientSecret = null;
	private static String oauthCode = null;

	public DestinyAPI setApiKey(String apiKey) {
		DestinyAPI.apiKey = apiKey;
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


	public BungieUser getUser(String id) { return new BungieUser(id); }

	/**
	 * Gets the users with this name (There can be multiple users with the same name)
	 */
	public List<BungieUser> getUsersWithName(String name) {
		HttpUtils hu = new HttpUtils();
		List<BungieUser> temp = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		try {
			String url = "https://www.bungie.net/platform/Destiny2/SearchDestinyPlayer/-1/" + name.replace(" ", "%20");
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("X-API-KEY", apiKey);

			InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			JsonElement parse = new JsonParser().parse(reader);
			JsonObject obj = parse.getAsJsonObject();
			if (obj.get("Response").isJsonArray()) {
				for (JsonElement objj : obj.getAsJsonArray("Response")) {
					JsonObject us = objj.getAsJsonObject();
					BungieUser bu = new BungieUser(us.get("membershipId").getAsString());
					if(!ids.contains(bu.getBungieMembershipID())) {
						temp.add(bu);
						ids.add(bu.getBungieMembershipID());
					}
				}
		}
	} catch (IOException ioException) {
		ioException.printStackTrace();
	}
		return temp;
}

	public Clan getClan(long id) {
		return new Clan(id);
	}

	public Clan getClan(String name) {
		return new Clan(name);
	}

	public static String getApiKey() { return apiKey; }

	public static String getClientId() { return clientId; }

	public static String getClientSecret() { return clientSecret; }

	public static String getOauthCode() { return oauthCode; }

}