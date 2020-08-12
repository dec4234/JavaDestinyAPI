package material.stats;

import com.google.gson.JsonObject;
import material.user.BungieUser;
import material.user.Character;
import org.jetbrains.annotations.NotNull;
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
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Stats/").getAsJsonObject("Response").getAsJsonObject("mergedAllCharacters").getAsJsonObject("results");
		allPve = jo.getAsJsonObject("allPvE").getAsJsonObject("allTime");
	}

	/**
	 * Gets stats for this user's specific character
	 */
	public UserStats(BungieUser bungieUser, Character character) {
		this.bungieUser = bungieUser;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + character.getCharacterID() + "/Stats/").getAsJsonObject("Response");
		allPve = jo.getAsJsonObject("allPvE").getAsJsonObject("allTime");
	}

	public int getActivitiesCleared() {
		if(activitiesCleared != 0) return activitiesCleared;
		activitiesCleared = getBasicPVE("activitiesCleared").get("value").getAsInt();
		return activitiesCleared;
	}

	public int getActivitiesEntered() {
		if(activitiesEntered != 0) return activitiesEntered;
		activitiesEntered = getBasicPVE("activitiesEntered").get("value").getAsInt();
		return activitiesEntered;
	}

	public int getAssists() {
		if(assists != 0) return assists;
		assists = getBasicPVE("assists").get("value").getAsInt();
		return assists;
	}

	public double getPgaAssits() {
		if(pgaAssits != 0) return pgaAssits;
		pgaAssits = getPGAPVE("assists").get("displayValue").getAsDouble();
		return pgaAssits;
	}

	public int getTotalKillDistance() {
		if(totalKillDistance != 0) return totalKillDistance;
		totalKillDistance = getBasicPVE("totalKillDistance").get("value").getAsInt();
		return totalKillDistance;
	}

	public int getKills() {
		if(kills != 0) return kills;
		kills = getBasicPVE("kills").get("value").getAsInt();
		return kills;
	}

	public double getPgaKills() {
		if(pgaKills != 0) return pgaKills;
		pgaKills = getPGAPVE("kills").get("displayValue").getAsDouble();
		return pgaKills;
	}

	public double getAverageKillDistance() {
		if(averageKillDistance != 0) return averageKillDistance;
		averageKillDistance = getBasicPVE("averageKillDistance").get("displayValue").getAsDouble();
		return averageKillDistance;
	}

	public long getSecondsPlayed() {
		if(secondsPlayed != 0) return secondsPlayed;
		secondsPlayed = getBasicPVE("secondsPlayed").get("value").getAsLong();
		return secondsPlayed;
	}

	public double getPgaSecondsPlayed() {
		if(pgaSecondsPlayed != 0) return pgaSecondsPlayed;
		pgaSecondsPlayed = Double.parseDouble(df.format(getPGAPVE("secondsPlayed").get("value").getAsDouble()));
		return pgaSecondsPlayed;
	}

	public int getDeaths() {
		if(deaths != 0) return deaths;
		deaths = getBasicPVE("deaths").get("value").getAsInt();
		return deaths;
	}

	public double getPgaDeaths() {
		if(pgaDeaths != 0) return pgaDeaths;
		pgaDeaths = Double.parseDouble(df.format(getPGAPVE("deaths").get("value").getAsDouble()));
		return pgaDeaths;
	}




	// util methods
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