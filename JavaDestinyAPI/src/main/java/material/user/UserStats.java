package material.user;

import com.google.gson.JsonObject;
import utils.HttpUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class UserStats {

	HttpUtils hu = new HttpUtils();
	BungieUser bungieUser;
	Character character;
	JsonObject jo;
	JsonObject allPve;
	DecimalFormat df = new DecimalFormat("##.00");

	private int activitiesCleared, activitiesEntered, assists, totalKillDistance, kills, deaths;
	private double pgaAssits, pgaKills, averageKillDistance, pgaSecondsPlayed, pgaDeaths;
	private long secondsPlayed;

	/**
	 * Gets stats for this user's entire account
	 */
	public UserStats(BungieUser bungieUser) {
		this.bungieUser = bungieUser;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Stats/");
		allPve = jo.getAsJsonObject("allPve").getAsJsonObject("allTime");
		assignValues();
	}

	/**
	 * Gets stats for this user's specific character
	 */
	public UserStats(BungieUser bungieUser, Character character) {
		this.bungieUser = bungieUser;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + character.getCharacterID() + "/Stats/").getAsJsonObject("Response").getAsJsonObject("mergedAllCharacters").getAsJsonObject("results");
		allPve = jo.getAsJsonObject("allPve").getAsJsonObject("allTime");
		assignValues();
	}

	private void assignValues() {
		activitiesCleared = getBasicPVE("activitiesCleared").get("value").getAsInt();
		activitiesEntered = getBasicPVE("activitiesEntered").get("value").getAsInt();
		assists = getBasicPVE("assists").getAsJsonObject("value").getAsInt();
		pgaAssits = getPGAPVE("assists").get("displayValue").getAsDouble();
		totalKillDistance = getBasicPVE("totalKillDistance").get("value").getAsInt();
		kills = getBasicPVE("kills").get("value").getAsInt();
		pgaKills = getPGAPVE("kills").get("displayValue").getAsDouble();
		averageKillDistance = getBasicPVE("averageKillDistance").get("displayValue").getAsDouble();
		secondsPlayed = getBasicPVE("secondsPlayed").get("value").getAsLong();
		pgaSecondsPlayed = Double.parseDouble(df.format(getPGAPVE("secondsPlayed").get("value").getAsDouble()));
		deaths = getBasicPVE("deaths").get("value").getAsInt();
		pgaDeaths = Double.parseDouble(df.format(getPGAPVE("deaths").get("value").getAsDouble()));
	}

	public double getPgaSecondsPlayed() {
		return pgaSecondsPlayed;
	}

	public JsonObject getJsonObject() {
		return jo;
	}

	private JsonObject getBasicPVE(String name) {
		return allPve.getAsJsonObject(name).getAsJsonObject("basic");
	}

	private JsonObject getPGAPVE(String name) {
		return allPve.getAsJsonObject(name).getAsJsonObject("pga");
	}
}
