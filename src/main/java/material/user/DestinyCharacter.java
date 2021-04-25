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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DestinyCharacter {

	private String membershipID;
	private String characterID;
	private Date lastPlayed;
	private String minutesPlayedThisSession;
	private String minutesPlayedTotal;
	private String lightLevel;
	private String membershipType;

	private Gender gender;
	private DestinyClass d2class;
	private Race race;

	private String emblemPath;
	private String emblemBackgroundPath;
	private String emblemHash;

	private List<Activity> allActivities;

	HttpUtils hu = new HttpUtils();
	private JsonObject jo;

	public DestinyCharacter(BungieUser bungieUser, String characterID) {
		this.characterID = characterID;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Profile/" + bungieUser.getBungieMembershipID() + "/Character/" + characterID + "/?components=200").getAsJsonObject("Response").getAsJsonObject("character").getAsJsonObject("data");
		assignValues();
	}

	private void assignValues() {
		membershipID = jo.get("membershipId").getAsString();
		membershipType = jo.get("membershipType").getAsString();
		characterID = jo.get("characterId").getAsString();
		lastPlayed = StringUtils.valueOfZTime(jo.get("dateLastPlayed").getAsString());
		minutesPlayedThisSession = jo.get("minutesPlayedThisSession").getAsString();
		minutesPlayedTotal = jo.get("minutesPlayedTotal").getAsString();
		lightLevel = jo.get("light").getAsString();

		race = evaluateRace(jo.get("raceHash").getAsString());
		gender = evaluateGender(jo.get("genderHash").getAsString());
		d2class = evaluateClass(jo.get("classHash").getAsString());

		emblemPath = jo.get("emblemPath").getAsString();
		emblemBackgroundPath = jo.get("emblemBackgroundPath").getAsString();
		emblemHash = jo.get("emblemHash").getAsString();
	}

	public String getMembershipID() { return membershipID; }
	public String getMembershipType() { return membershipType; }
	public String getCharacterID() { return characterID; }
	public Date getLastPlayed() { return lastPlayed; }
	public String getMinutesPlayedThisSession() { return minutesPlayedThisSession; }
	public String getMinutesPlayedTotal() { return minutesPlayedTotal; }
	public String getLightLevel() { return lightLevel; }

	public Race getRace() { return race; }
	public Gender getGender() { return gender; }
	public DestinyClass getD2class() { return d2class; }

	public String getEmblemPath() { return emblemPath; }
	public String getEmblemBackgroundPath() { return emblemBackgroundPath; }
	public String getEmblemHash() { return emblemHash; }
	public JsonObject getJsonObject() { return jo; }

	public List<DestinyItem> getEquippedItems() {
		JsonArray jsonArray = new HttpUtils().urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + membershipID + "/Character/"
				+ getCharacterID() + "/?components=205").getAsJsonObject("Response").getAsJsonObject("equipment").getAsJsonObject("data").getAsJsonArray("items");

		List<DestinyItem> destinyItems = new ArrayList<>();

		for(JsonElement jsonElement : jsonArray) {
			destinyItems.add(new DestinyItem(jsonElement.getAsJsonObject().get("itemHash").getAsString()));
		}

		return destinyItems;
	}

	/**
	 * A very resource intensive task, use at your own risk
	 * Needs work because not all activities return the same JSON info
=	 */
	public List<Activity> getAllActivities() {
		if(allActivities != null) return allActivities;
		allActivities = new ArrayList<>();
		JsonObject jj = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Account/" + getMembershipID() + "/Character/" + getCharacterID() + "/Stats/AggregateActivityStats/");
		for(JsonElement je : jj.getAsJsonObject("Response").getAsJsonArray("activities")) {
			allActivities.add(new Activity(je.getAsJsonObject().getAsJsonObject("values").getAsJsonObject("fastestCompletionMsForActivity").get("activityId").getAsString()));
		}
		return allActivities;
	}

	private Gender evaluateGender(String genderHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.GENDER, genderHash).getAsJsonObject("Response");
		switch(jj.get("genderType").getAsString()) {
			case "0":
				return Gender.MALE;
			case "1":
				return Gender.FEMALE;
		}
		return null;
	}

	private DestinyClass evaluateClass(String classHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.CLASS, classHash).getAsJsonObject("Response");
		switch(jj.getAsJsonObject("displayProperties").get("name").getAsString()) {
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
		switch(jj.getAsJsonObject("displayProperties").get("name").getAsString()) {
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
