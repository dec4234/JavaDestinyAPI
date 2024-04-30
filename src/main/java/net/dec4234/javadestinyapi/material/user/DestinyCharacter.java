/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.inventory.items.DestinyItem;
import net.dec4234.javadestinyapi.material.inventory.items.InventoryItem;
import net.dec4234.javadestinyapi.material.inventory.loadouts.Loadout;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;
import net.dec4234.javadestinyapi.stats.activities.Activity;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.StringUtils;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DestinyCharacter is a character on a user's account like a Warlock, Titan, or Hunter.
 * <br>
 * There is a lot of character-specific information that is captured within this class.
 */
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

	HttpUtils hu = DestinyAPI.getHttpUtils();

	public DestinyCharacter(BungieUser bungieUser, String characterID) throws APIException {
		super("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Profile/" + bungieUser.getID() + "/Character/" + characterID + "/?components=200",
			  source -> source.getAsJsonObject("Response").getAsJsonObject("character").getAsJsonObject("data"));
		this.characterID = characterID;
		this.bungieUser = bungieUser;
	}

	protected DestinyCharacter(JsonObject jsonObject, BungieUser bungieUser, String characterID) {
		super(jsonObject);
		jo = jsonObject;
		this.characterID = characterID;
		this.bungieUser = bungieUser;
	}

	/**
	 * Get the membership ID of the account that owns this character
	 */
	public String getMembershipID() {
		return bungieUser.getID();
	}

	/**
	 * Get the membershipType of the account that owns this character
	 */
	public int getMembershipType() throws APIException {
		return bungieUser.getMembershipType();
	}

	/**
	 * Get the BungieUser that owns this account
	 */
	public BungieUser getBungieUser() { return bungieUser; }

	/**
	 * Get the characterID of this character
	 */
	public String getCharacterID() { return characterID; }

	/**
	 * Return the character id as a long, aka int64
	 */
	public Long getCharacterIDAsLong() { return Long.parseLong(getCharacterID()); }

	/**
	 * Get the Date that this character was last played
	 */
	public Date getLastPlayed() throws APIException {
		if (lastPlayed == null) {
			lastPlayed = StringUtils.valueOfZTime(getJO().get("dateLastPlayed").getAsString());
		}
		return lastPlayed;
	}

	/**
	 * Get the total amount of time, in minutes, that this character has been played in this session
	 * TO-DO: Define what a "session" is
	 */
	public int getMinutesPlayedThisSession() throws APIException {
		if (minutesPlayedThisSession == -1) {
			minutesPlayedThisSession = getJO().get("minutesPlayedThisSession").getAsInt();
		}
		return minutesPlayedThisSession;
	}

	/**
	 * Get the total amount of time, in minutes, that has been played on this character
	 */
	public String getMinutesPlayedTotal() throws APIException {
		if (minutesPlayedTotal == null) {
			minutesPlayedTotal = getJO().get("minutesPlayedTotal").getAsString();
		}
		return minutesPlayedTotal;
	}

	/**
	 * Get the light level of this character
	 *
	 * @return The light level of this character, as an integer
	 */
	public int getLightLevel() throws APIException {
		if (lightLevel == -1) {
			lightLevel = getJO().get("light").getAsInt();
		}
		return lightLevel;
	}

	/**
	 * Get the race of this character
	 * Either Human, Exo or Awoken
	 */
	public Race getRace() throws APIException {
		if (race == null) {
			race = evaluateRace(getJO().get("raceHash").getAsString());
		}
		return race;
	}

	/**
	 * Get the gender of this character
	 *
	 * @return Male or Female
	 */
	public Gender getGender() throws APIException {
		if (gender == null) {
			gender = evaluateGender(getJO().get("genderHash").getAsString());
		}
		return gender;
	}

	/**
	 * Get the Class of this character
	 * Either Warlock, Titan or Hunter
	 */
	public DestinyClass getD2class() throws APIException {
		if (d2class == null) {
			d2class = evaluateClass(getJO().get("classHash").getAsString());
		}
		return d2class;
	}

	/**
	 * Get the currently equipped emblem of this character
	 * Add this path to the end of "https://bungie.net/"
	 *
	 * @return
	 */
	public String getEmblemPath() throws APIException {
		if (emblemPath == null) {
			emblemPath = getJO().get("emblemPath").getAsString();
		}
		return emblemPath;
	}

	/**
	 * Get the background of the currently equipped emblem
	 * Add this path to the end of "https://bungie.net/"
	 */
	public String getEmblemBackgroundPath() throws APIException {
		if (emblemBackgroundPath == null) {
			emblemBackgroundPath = getJO().get("emblemBackgroundPath").getAsString();
		}
		return emblemBackgroundPath;
	}

	/**
	 * Get the hash of the currently equipped emblem to be used in a manifest request
	 */
	public String getEmblemHash() throws APIException {
		if (emblemHash == null) {
			emblemHash = getJO().get("emblemHash").getAsString();
		}
		return emblemHash;
	}

	/**
	 * Get a list of the currently equipped items of this character
	 */
	public List<DestinyItem> getEquippedItems() throws APIException {
		JsonArray jsonArray = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieUser.getID() + "/Character/"
													   + getCharacterID() + "/?components=205").getAsJsonObject("Response").getAsJsonObject("equipment").getAsJsonObject("data").getAsJsonArray("items");

		List<DestinyItem> destinyItems = new ArrayList<>();

		for (JsonElement jsonElement : jsonArray) {
			destinyItems.add(new DestinyItem(jsonElement.getAsJsonObject().get("itemHash").getAsString()));
		}

		return destinyItems;
	}

	/*
	public InventoryItem getItemInSlot(InventoryItem.ItemLocation itemLocation) {
		for(DestinyItem destinyItem : getEquippedItems()) {
			return destinyItem;
		}
	}
	 */

	/**
	 * Get a list of inventory items present in this character's inventory.
	 * <br>
	 * This is an OAUTH action that required inventory reading permission.
	 * @return A list of inventory items, equipped or unequipped
	 */
	public List<InventoryItem> getAllItemsInInventory() throws APIException {
		JsonArray jsonArray = hu.urlRequestGETOauth("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieUser.getID() + "/Character/"
															+ getCharacterID() + "/?components=201").getAsJsonObject("Response").getAsJsonObject("inventory").getAsJsonObject("data").getAsJsonArray("items");

		List<InventoryItem> list = new ArrayList<>();

		for (JsonElement jsonElement : jsonArray) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();

			if (jsonObject.has("itemInstanceId")) {
				list.add(new InventoryItem(jsonObject.get("itemHash").getAsString(),
										   jsonObject.get("itemInstanceId").getAsString(),
						 this,
						 jsonObject.get("quantity").getAsInt(),
						 jsonObject.get("bindStatus").getAsInt(),
						 jsonObject.get("location").getAsInt(),
						 jsonObject.get("bucketHash").getAsString(),
						 jsonObject.get("transferStatus").getAsInt(),
						 jsonObject.get("lockable").getAsBoolean(),
						 jsonObject.get("state").getAsInt(),
						 jsonObject.get("dismantlePermission").getAsInt(),
						 jsonObject.get("isWrapper").getAsBoolean()));
			}
		}

		return list;
	}

	/**
	 * TODO: complete
	 * <br>
	 * This function is currently incomplete. If you would like to contribute, please create a pull request on GitHub
	 * @return A list of loadouts on this character
	 */
	public List<Loadout> getLoadouts() throws APIException {
		hu.urlRequestGETOauth(HttpUtils.URL_BASE + "/Destiny2/" + getMembershipType() + "/Profile/" + bungieUser.getID() + "/Character/" + getCharacterID() + "/?components=206,201");

		List<Loadout> loadouts = new ArrayList<>();



		return loadouts;
	}

	/**
	 * A very resource intensive task, use at your own risk
	 * Needs work because not all activities return the same JSON info
	 * =
	 */
	public List<Activity> getAllActivities() throws APIException {
		if (allActivities != null) { return allActivities; }
		allActivities = new ArrayList<>();
		JsonObject jj = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Account/" + getMembershipID() + "/Character/" + getCharacterID() + "/Stats/AggregateActivityStats/");
		for (JsonElement je : jj.getAsJsonObject("Response").getAsJsonArray("activities")) {
			allActivities.add(new Activity(je.getAsJsonObject().getAsJsonObject("values").getAsJsonObject("fastestCompletionMsForActivity").get("activityId").getAsString()));
		}
		return allActivities;
	}


	private boolean hasPlayedInActivity(String pgcrId, List<Activity> source) throws APIException {
		for (Activity activity : source) {
			if (pgcrId.equals(activity.getInstanceId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Has this character played in the activity with the specified pgcrID
	 *
	 * @param pgcrId The ID of the activity you would like to be analyzed
	 *
	 * @return True or false depending on if the user played in it
	 */
	public boolean hasPlayedInActivity(String pgcrId) throws APIException {
		List<String> participantIds = new ArrayList<>();

		new Activity(pgcrId).getParticipants().forEach(activityParticipant -> {
			participantIds.add(activityParticipant.getCharacterId());
		});

		return participantIds.contains(getCharacterID());
	}

	private Gender evaluateGender(String genderHash) throws APIException {
		JsonObject jj = hu.manifestGET(ManifestEntityTypes.GENDER, genderHash).getAsJsonObject("Response");
		switch (jj.get("genderType").getAsString()) {
			case "0":
				return Gender.MALE;
			case "1":
				return Gender.FEMALE;
		}
		return null;
	}

	private DestinyClass evaluateClass(String classHash) throws APIException {
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

	private Race evaluateRace(String raceHash) throws APIException {
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

	/**
	 * Gender of this character. Male or Female
	 */
	public enum Gender {
		MALE("Male"),
		FEMALE("Female");

		private String value;

		private Gender(String value) {
			this.value = value;
		}

		public String getValue() { return value; }

	}

	/**
	 * The class of this character. Hunter, Titan or Warlock
	 */
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

	/**
	 * The race of this character. Awoken, Exo or Human
	 */
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
