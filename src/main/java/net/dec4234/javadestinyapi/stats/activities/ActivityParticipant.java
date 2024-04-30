/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.stats.activities;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.user.BungieUser;

public class ActivityParticipant {

	private JsonObject jo;
	private BungieUser bungieUser;
	private String membershipId, characterId;
	private int score, assists, deaths, kills, opponentsDefeated, activityDuration, completionReason;
	private double kdr, kda, completed, efficiency, startSeconds, timePlayed;
	JsonObject values;

	public ActivityParticipant(JsonObject entry) {
		jo = entry;
	}

	private void assignValues() {

	}

	/**
	 * Get the membership ID of this user
	 */
	public String getMembershipId() {
		if (membershipId != null) return membershipId;
		membershipId = jo.getAsJsonObject("player").getAsJsonObject("destinyUserInfo").get("membershipId").getAsString();
		return membershipId;
	}

	/**
	 * Gets the bungie user of this
=	 */
	public BungieUser getBungieUser() {
		if (bungieUser != null) return bungieUser;
		bungieUser = new BungieUser(getMembershipId());
		return bungieUser;
	}

	/**
	 * Get the character ID of the player
=	 */
	public String getCharacterId() {
		if (characterId != null) return characterId;
		characterId = jo.get("characterId").getAsString();
		return characterId;
	}

	/**
	 * Gets the score of the user in this activity
	 */
	public int getScore() {
		if (score != 0) return score;
		score = jo.getAsJsonObject("score").getAsJsonObject("basic").get("value").getAsInt();
		return score;
	}

	/**
	 * Get the assists of the user
-	 */
	public int getAssists() {
		if (assists != 0) return assists;
		assists = jo.getAsJsonObject("assists").getAsJsonObject("basic").get("value").getAsInt();
		return assists;
	}

	/**
	 * Get if this user completed the activity
-	 */
	public double getCompleted() {
		if(completed != 0) return completed;
		completed = jo.getAsJsonObject("completed").getAsJsonObject("basic").get("value").getAsDouble();
		return completed;
	}

	/**
	 * Get the number of deaths for this user
	 */
	public int getDeaths() {
		if(deaths != 0) return deaths;
		deaths = jo.getAsJsonObject("deaths").getAsJsonObject("basic").get("value").getAsInt();
		return deaths;
	}

	/**
	 * Get the number of kills for this user
	 */
	public int getKills() {
		if(kills != 0) return kills;
		kills = jo.getAsJsonObject("kills").getAsJsonObject("basic").get("value").getAsInt();
		return kills;
	}

	/**
	 * Gets the number of opponents this user defeated
-	 */
	public int getOpponentsDefeated() {
		if(opponentsDefeated != 0) return opponentsDefeated;
		opponentsDefeated = jo.getAsJsonObject("opponentsDefeated").getAsJsonObject("basic").get("value").getAsInt();
		return opponentsDefeated;
	}

	/**
	 * Get the efficiency of this user
	 */
	public double getEfficiency() {
		if(efficiency != 0) return efficiency;
		efficiency = jo.getAsJsonObject("efficiency").getAsJsonObject("basic").get("value").getAsDouble();
		return efficiency;
	}

	/**
	 * Gets the Kill-Death Ratio of this user
	 */
	public double getKdr() {
		if(kdr != 0) return kdr;
		kdr = jo.getAsJsonObject("killsDeathsRatio").getAsJsonObject("basic").get("value").getAsDouble();
		return kdr;
	}

	/**
	 * Gets the Kill-Deaths-Assists of this user
	 */
	public double getKda() {
		if(kda != 0) return kda;
		kda = jo.getAsJsonObject("killsDeathsAssists").getAsJsonObject("basic").get("value").getAsDouble();
		return kda;
	}

	/**
	 * Gets the duration, in seconds, of this activity
	 */
	public int getActivityDuration() {
		if(activityDuration != 0) return activityDuration;
		activityDuration = jo.getAsJsonObject("activityDurationSeconds").getAsJsonObject("basic").get("value").getAsInt();
		return activityDuration;
	}

	/**
	 * Gets the reason why this activity was completed
	 */
	public int getCompletionReason() {
		if(completionReason != 0) return completionReason;
		completionReason = jo.getAsJsonObject("completionReason").getAsJsonObject("basic").get("value").getAsInt();
		return completionReason;
	}

	/**
	 * Gets the time the user joined the activity?
	 */
	public double getStartSeconds() {
		if(startSeconds != 0) return startSeconds;
		startSeconds = jo.getAsJsonObject("startSeconds").getAsJsonObject("basic").get("value").getAsDouble();
		return startSeconds;
	}

	/**
	 * Gets how long this user played in this activity
	 */
	public double getTimePlayed() {
		if(timePlayed != 0) return timePlayed;
		timePlayed = jo.getAsJsonObject("timePlayedSeconds").getAsJsonObject("basic").get("value").getAsDouble();
		return timePlayed;
	}

	public JsonObject getJO() {
		return jo;
	}
}
