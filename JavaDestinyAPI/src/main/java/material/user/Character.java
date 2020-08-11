package material.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.manifest.ManifestEntityTypes;
import material.stats.Activity;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Character {

	private String membershipID;
	private String characterID;
	private Date lastPlayed;
	private String minutesPlayedThisSession;
	private String minutesPlayedTotal;
	private String lightLevel;
	private String membershipType;

	private Gender gender;
	private Class d2class;
	private Race race;

	private String emblemPath;
	private String emblemBackgroungPath;
	private String emblemHash;

	private List<Activity> allActivities;

	HttpUtils hu = new HttpUtils();
	private JsonObject jo;

	public Character(BungieUser bungieUser, String characterID) {
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
		emblemBackgroungPath = jo.get("emblemBackgroundPath").getAsString();
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
	public Class getD2class() { return d2class; }

	public String getEmblemPath() { return emblemPath; }
	public String getEmblemBackgroungPath() { return emblemBackgroungPath; }
	public String getEmblemHash() { return emblemHash; }

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

	private Class evaluateClass(String classHash) {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.CLASS, classHash).getAsJsonObject("Response");
		switch(jj.getAsJsonObject("displayProperties").get("name").getAsString()) {
			case "Warlock":
				return Class.WARLOCK;
			case "Titan":
				return Class.TITAN;
			case "Hunter":
				return Class.HUNTER;
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

	enum Gender {
		MALE("Male"),
		FEMALE("Female");

		private String value;
		private Gender(String value) {
			this.value = value;
		}
		public String getValue() { return value; }

	}

	enum Class {
		HUNTER("Hunter"),
		TITAN("Titan"),
		WARLOCK("Warlock");

		private String value;
		private Class(String value) {
			this.value = value;
		}
		public String getValue() { return value; }
	}

	enum Race {
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
