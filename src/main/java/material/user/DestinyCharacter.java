/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.inventory.DestinyItem;
import material.manifest.ManifestEntityTypes;
import material.stats.Activity;
import utils.HttpUtils;
import utils.StringUtils;
import utils.framework.ContentFramework;
import utils.framework.ContentInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DestinyCharacter extends ContentFramework {

	private BungieUser bungieUser;

	private String characterID, minutesPlayedTotal;
	private Date lastPlayed;
	private int minutesPlayedThisSession, lightLevel = -1;

	private Gender gender;
	private DestinyClass d2class;
	private Race race;

	private String emblemPath, emblemBackgroundPath, emblemHash;

	private List<Activity> allActivities;

	HttpUtils hu = new HttpUtils();

	public DestinyCharacter(BungieUser bungieUser, String characterID) {
		super("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Profile/" + bungieUser.getBungieMembershipID() + "/Character/" + characterID + "/?components=200",
			  source -> source.getAsJsonObject("Response").getAsJsonObject("character").getAsJsonObject("data"));
		this.characterID = characterID;
		this.bungieUser = bungieUser;
	}

	public String getMembershipID() {
		return bungieUser.getBungieMembershipID();
	}

	public int getMembershipType() {
		return bungieUser.getMembershipType();
	}

	public String getCharacterID() { return characterID; }

	public Date getLastPlayed() {
		if (lastPlayed == null) {
			lastPlayed = StringUtils.valueOfZTime(getJO().get("dateLastPlayed").getAsString());
		}
		return lastPlayed;
	}

	public int getMinutesPlayedThisSession() {
		if (minutesPlayedThisSession == -1) {
			minutesPlayedThisSession = getJO().get("minutesPlayedThisSession").getAsInt();
		}
		return minutesPlayedThisSession;
	}

	public String getMinutesPlayedTotal() {
		if (minutesPlayedTotal == null) {
			minutesPlayedTotal = getJO().get("minutesPlayedTotal").getAsString();
		}
		return minutesPlayedTotal;
	}

	public int getLightLevel() {
		if (lightLevel == -1) {
			lightLevel = getJO().get("light").getAsInt();
		}
		return lightLevel;
	}

	public Race getRace() {
		if (race == null) {
			race = evaluateRace(getJO().get("raceHash").getAsString());
		}
		return race;
	}

	public Gender getGender() {
		if (gender == null) {
			gender = evaluateGender(getJO().get("genderHash").getAsString());
		}
		return gender;
	}

	public DestinyClass getD2class() {
		if (d2class == null) {
			d2class = evaluateClass(getJO().get("classHash").getAsString());
		}
		return d2class;
	}

	public String getEmblemPath() {
		if (emblemPath == null) {
			emblemPath = getJO().get("emblemPath").getAsString();
		}
		return emblemPath;
	}

	public String getEmblemBackgroundPath() {
		if (emblemBackgroundPath == null) {
			emblemBackgroundPath = getJO().get("emblemBackgroundPath").getAsString();
		}
		return emblemBackgroundPath;
	}

	public String getEmblemHash() {
		if (emblemHash == null) {
			emblemHash = getJO().get("emblemHash").getAsString();
		}
		return emblemHash;
	}

	public JsonObject getJsonObject() { return getJO(); }

	public List<DestinyItem> getEquippedItems() {
		JsonArray jsonArray = new HttpUtils().urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieUser.getBungieMembershipID() + "/Character/"
																	+ getCharacterID() + "/?components=205").getAsJsonObject("Response").getAsJsonObject("equipment").getAsJsonObject("data").getAsJsonArray("items");

		List<DestinyItem> destinyItems = new ArrayList<>();

		for (JsonElement jsonElement : jsonArray) {
			destinyItems.add(new DestinyItem(jsonElement.getAsJsonObject().get("itemHash").getAsString()));
		}

		return destinyItems;
	}

	/**
	 * A very resource intensive task, use at your own risk
	 * Needs work because not all activities return the same JSON info
	 * =
	 */
	public List<Activity> getAllActivities() {
		if (allActivities != null) { return allActivities; }
		allActivities = new ArrayList<>();
		JsonObject jj = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Account/" + getMembershipID() + "/Character/" + getCharacterID() + "/Stats/AggregateActivityStats/");
		for (JsonElement je : jj.getAsJsonObject("Response").getAsJsonArray("activities")) {
			allActivities.add(new Activity(je.getAsJsonObject().getAsJsonObject("values").getAsJsonObject("fastestCompletionMsForActivity").get("activityId").getAsString()));
		}
		return allActivities;
	}

	private Gender evaluateGender(String genderHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.GENDER, genderHash).getAsJsonObject("Response");
		switch (jj.get("genderType").getAsString()) {
			case "0":
				return Gender.MALE;
			case "1":
				return Gender.FEMALE;
		}
		return null;
	}

	private DestinyClass evaluateClass(String classHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.CLASS, classHash).getAsJsonObject("Response");
		switch (jj.getAsJsonObject("displayProperties").get("name").getAsString()) {
			case "Warlock":
				return DestinyClass.WARLOCK;
			case "Titan":
				return DestinyClass.TITAN;
			case "Hunter":
				return DestinyClass.HUNTER;
		}
		return null;
	}

	private Race evaluateRace(String raceHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.RACE, raceHash).getAsJsonObject("Response");
		switch (jj.getAsJsonObject("displayProperties").get("name").getAsString()) {
			case "Exo":
				return Race.EXO;
			case "Awoken":
				return Race.AWOKEN;
			case "Human":
				return Race.HUMAN;
		}
		return null;
	}

	public enum Gender {
		MALE("Male"),
		FEMALE("Female");

		private String value;

		private Gender(String value) {
			this.value = value;
		}

		public String getValue() { return value; }

	}

	public enum DestinyClass {
		HUNTER("Hunter"),
		TITAN("Titan"),
		WARLOCK("Warlock");

		private String value;

		private DestinyClass(String value) {
			this.value = value;
		}

		public String getValue() { return value; }
	}

	public enum Race {
		AWOKEN("Awoken"),
		EXO("Exo"),
		HUMAN("Human");

		private String value;

		private Race(String value) {
			this.value = value;
		}

		public String getValue() { return value; }
	}
}
