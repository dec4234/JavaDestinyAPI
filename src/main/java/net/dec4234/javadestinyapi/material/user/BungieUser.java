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
import net.dec4234.javadestinyapi.material.clan.Clan;
import net.dec4234.javadestinyapi.material.inventory.CollectionsManager;
import net.dec4234.javadestinyapi.material.inventory.InventoryManager;
import net.dec4234.javadestinyapi.stats.activities.ActivityInfo;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.StringUtils;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A "BungieUser" is a Destiny 2 Player.
 * It is possible for there to be no information pertaining to a specific BungieUser so
 * call isValidUser() before anything else.
 * Most information for this class is pulled from the /LinkedProfiles/ Endpoint
 */
public class BungieUser extends ContentFramework {

	private String bungieMembershipID, displayName, globalDisplayName, supplementalDisplayName, discriminator;
	private Date lastPlayed;
	private JsonObject je; // The JsonObject of the profile used to get most information
	private HttpUtils hu = DestinyAPI.getHttpUtils();

	private ArrayList<Integer> applicableMembershipTypes = new ArrayList<>();

	private List<DestinyCharacter> destinyCharacters = null;
	private int playTime = -1, crossSaveOverride = -1, membershipType = -1;
	private boolean isPublic, isCrossSavePrimary, isOverridden = false;
	@Deprecated
	private int intendedPlatform = -2;
	private InventoryManager inventoryManager;
	private CollectionsManager collectionsManager;

	private Clan clan = null;

	/**
	 * The simplest constructor for starting a BungieUser, only requires the BungieID of the user
	 * <br>
	 * A BungieID is a static, unique id representing a bungie account.
	 * <br>
	 * Multiple destiny accounts (on separate platforms) can be associated with the same BungieID. Use
	 * membership type to specify which profile you want to examine.
	 *
	 * @param bungieMembershipID The BungieID of the user you want to examine
	 */
	public BungieUser(String bungieMembershipID) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
	}

	/**
	 * Uses a BungieID and a JsonObject of data about the user to construct the object more effeciently.
	 * <br>
	 * Useful for certain endpoints (mostly from the Clan section) where you may need to load data about a lot of users.
	 *
	 * @param bungieMembershipID The BungieID of the user you want to examine
	 * @param destinyMembership  A JsonObject containing data about the user, usually from a Bungie endpoint
	 */
	public BungieUser(String bungieMembershipID, JsonObject destinyMembership) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});

		this.bungieMembershipID = bungieMembershipID;
		this.displayName = destinyMembership.get("displayName").getAsString();
		this.globalDisplayName = destinyMembership.get("bungieGlobalDisplayName").getAsString();
		this.membershipType = destinyMembership.get("membershipType").getAsInt();
		this.crossSaveOverride = destinyMembership.get("crossSaveOverride").getAsInt();
		this.isPublic = destinyMembership.get("isPublic").getAsBoolean();
	}

	/**
	 * Used to provide a little more context for what profile the BungieUser class should pull info from
	 * because a LinkedProfiles request can have multiple platform accounts attached to it.
	 * <br>
	 * You are responsible for checking the getMembershipTypes() to see if that user has a profile on that platform.
	 * <br>
	 * Deprecated since cross save became a thing, likely no longer useful.
	 *
	 * @param bungieMembershipID The bungie Id of the user you want to get information for
	 * @param intendedPlatform   The platform that you want to pull information for
	 */
	@Deprecated
	public BungieUser(String bungieMembershipID, DestinyPlatform intendedPlatform) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
		this.intendedPlatform = intendedPlatform.getPlatformCode();
	}

	/**
	 * Construct the object will muliple fields from the get-go, thus improving efficiency.
	 *
	 * @param bungieMembershipID The bungie Id of the user you want to get information for
	 * @param displayName        The display name of the user in-game
	 * @param crossSaveOverride  The cross save override of the user's Bungie Account
	 * @param membershipType     The membership type of the profile you would like to examine
	 * @param isPublic           Marks if the Bungie Account is public or not
	 * @see net.dec4234.javadestinyapi.material.user.BungieUser#BungieUser(String, JsonObject)
	 */
	public BungieUser(String bungieMembershipID, String displayName, int crossSaveOverride, int membershipType, boolean isPublic) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
		this.displayName = displayName;
		this.crossSaveOverride = crossSaveOverride;
		this.membershipType = membershipType;
		this.isPublic = isPublic;
	}

	/**
	 * Construct the object will muliple fields from the get-go, thus improving efficiency.
	 *
	 * @param bungieMembershipID The bungie Id of the user you want to get information for
	 * @param displayName        The display name of the user in-game
	 * @param globalDisplayName  The global display name of the user, overrides all platform specific names
	 * @param crossSaveOverride  The cross save override of the user's Bungie Account
	 * @param membershipType     The membership type of the profile you would like to examine
	 * @param isPublic           Marks if the Bungie Account is public or not
	 * @see net.dec4234.javadestinyapi.material.user.BungieUser#BungieUser(String, JsonObject)
	 */
	public BungieUser(String bungieMembershipID, String displayName, String globalDisplayName, int crossSaveOverride, int membershipType, boolean isPublic) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
		this.displayName = displayName;
		this.globalDisplayName = globalDisplayName;
		this.crossSaveOverride = crossSaveOverride;
		this.membershipType = membershipType;
		this.isPublic = isPublic;
	}

	/**
	 * Gets the static, unqiue BungieID of the user
	 *
	 * @return The BungieID of the user
	 */
	public String getID() {
		return bungieMembershipID;
	}

	/**
	 * Determines whether or not the Bungie account has any Destiny profiles associated with it.
	 * <br>
	 * An account can be empty AKA "invalid" if it exists but does not have any Destiny profiles
	 *
	 * @return A boolean representing whether or not the Bungie User is valid
	 */
	@Deprecated
	public boolean isValidUser() throws APIException {
		try {
			return getJO().getAsJsonArray("profiles").size() != 0;
		} catch(NullPointerException nullPointerException) {
			return false;
		}
	}

	/**
	 * Gets the platform specific display name of the user.
	 * <br>
	 * Not too useful after the introduction of global display names.
	 * <br>
	 * Deprecated in favor of getGlobalDisplayName()
	 *
	 * @return The platform specifc display name for the current profile
	 * @see BungieUser#getGlobalDisplayName()
	 */
	@Deprecated
	public String getDisplayName() throws APIException {
		getJE();
		if(displayName == null) {
			displayName = getJE().get("displayName").getAsString();
		}
		return displayName;
	}

	/**
	 * Returns the Global Display Name of the user across all Destiny Platforms.
	 * <br>
	 * Please note, an empty string will be returned if a user has not logged in since the
	 * start of Season of The Lost.
	 *
	 * @return The global display name for this user, which is the same for all connected profiles
	 */
	public String getGlobalDisplayName() throws APIException {
		if (globalDisplayName == null) {

			// LinkedProfiles is not populated with bungieGlobalDisplayName as of 8/29/2021: github issue #1511
			// As far as I know, getSupplementalDisplayName is also the bungieGlobalDisplayName
			globalDisplayName = getJE().get("bungieGlobalDisplayName").getAsString();
		}

		return globalDisplayName;
	}

	/**
	 * Returns the combined displayname and user discriminator as used in friend requests and user searches.
	 * <br>
	 *
	 * @return The full global display name and discriminator for the current user: "dec4234#9904"
	 * @see BungieUser#getGlobalDisplayName()
	 * @see BungieUser#getDiscriminator()
	 */
	public String getSupplementalDisplayName() throws APIException {
		if (supplementalDisplayName == null) {
			supplementalDisplayName = getJO().getAsJsonObject("bnetMembership").get("supplementalDisplayName").getAsString();
		}

		return supplementalDisplayName;
	}

	/**
	 * Get the discriminator of a user's name
	 * <br>
	 *
	 * @return The discriminator of the user: E.g. "9904" of dec4234#9904
	 */
	public String getDiscriminator() throws APIException {
		if (discriminator == null) {
			discriminator = getSupplementalDisplayName().split("#")[1];
		}

		return discriminator;
	}

	/**
	 * Gets the last day this user was seen online.
	 *
	 * @return The date the last user was seen online
	 * @see BungieUser#getDaysSinceLastPlayed()
	 */
	public Date getLastPlayed() throws APIException {
		if (lastPlayed == null) {
			lastPlayed = StringUtils.valueOfZTime(getJE().get("dateLastPlayed").getAsString());
		}

		return lastPlayed;
	}

	/**
	 * Gets a double representing the number of days since the user last played.
	 *
	 * @return A double, representing the number of days from the current day to when this user was last seen online
	 * @see BungieUser#getLastPlayed()
	 */
	public double getDaysSinceLastPlayed() throws APIException {
		DecimalFormat df = new DecimalFormat("0.##");
		return Double.parseDouble(df.format((new Date().getTime() - getLastPlayed().getTime()) / 1000.0 / 60.0 / 60.0 / 24.0));
	}

	/**
	 * Determines whether the current profile was overriden by cross save
	 *
	 * @return A boolean saying if the profile was overriden by cross save
	 */
	public boolean isOverridden() throws APIException {
		return !isOverridden ? isOverridden = getJE().get("isOverridden").getAsBoolean() : isOverridden;
	}

	/**
	 * Determines whether the current profile is the primary profile on an account with cross save active
	 *
	 * @return A boolean representing whether or not the current profile is a cross save primary
	 */
	public boolean isCrossSavePrimary() throws APIException {
		return !isCrossSavePrimary ? isCrossSavePrimary = getJE().get("isCrossSavePrimary").getAsBoolean() : isCrossSavePrimary;
	}

	/**
	 * @return An integer representing the membership type of the profile that overrode this profile from cross save
	 */
	public int getCrossSaveOverride() throws APIException {
		return crossSaveOverride == -1 ? crossSaveOverride = getJE().get("crossSaveOverride").getAsInt() : crossSaveOverride;
	}

	/**
	 * Returns all applicable membership types for the current account, due to cross save
	 *
	 * @return A list of integers representing the membership types for applicable active profiles
	 */
	public ArrayList<Integer> getApplicableMembershipTypes() throws APIException {
		if (applicableMembershipTypes.isEmpty()) {
			for (JsonElement jj : getJE().get("applicableMembershipTypes").getAsJsonArray()) {
				applicableMembershipTypes.add(jj.getAsInt());
			}
		}
		return applicableMembershipTypes;
	}

	/**
	 * Get all membership types declared under this account's profiles
	 */
	public ArrayList<Integer> getMembershipTypes() throws APIException {
		ArrayList<Integer> integers = new ArrayList<>();

		for(JsonElement jsonElement : getJO().get("profiles").getAsJsonArray()) {
			integers.add(jsonElement.getAsJsonObject().get("membershipType").getAsInt());
		}

		return integers;
	}

	public boolean isPublic() throws APIException {
		return !isPublic ? isPublic = getJE().get("isPublic").getAsBoolean() : isPublic;
	}

	/**
	 * Returns the membership type of the main profile
	 */
	public int getMembershipType() throws APIException {
		return membershipType == -1 ? membershipType = getJE().get("membershipType").getAsInt() : membershipType;
	}

	/**
	 * Gets the characters that are attached to this BungieUser
	 *
	 * @return The list of characters associated with this account, or null if none are found
	 */
	public List<DestinyCharacter> getCharacters() throws APIException {
		if (destinyCharacters != null) { return destinyCharacters; }
		destinyCharacters = new ArrayList<>();
		JsonObject jsonObject = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + getID() + "/?components=Profiles,Characters").getAsJsonObject("Response");
		JsonArray ja = jsonObject.getAsJsonObject("profile").getAsJsonObject("data").getAsJsonArray("characterIds");

		if(ja == null || ja.size() == 0) {
			return null;
		}

		for(JsonElement je : ja) {
			String id = je.getAsString();
			destinyCharacters.add(new DestinyCharacter(jsonObject.getAsJsonObject("characters").getAsJsonObject("data").getAsJsonObject(id), this, id));
			// destinyCharacters.add(new DestinyCharacter(this, je.getAsString()));
		}

		return destinyCharacters;
	}

	/**
	 * Returns the character under this Bungie account that matches the Class provided.
	 * <br>
	 * This works well only if the user has 1 character of each type. If a user has two warlocks
	 * for example, it will throw an IllegalStateException because it cannot choose between them.
	 *
	 * @param destinyClass The Destiny class that you want to search for.
	 * @return The character if there is one, and is not a duplicate
	 */
	public DestinyCharacter getCharacterOfType(DestinyCharacter.DestinyClass destinyClass) throws APIException {
		DestinyCharacter toReturn = null;

		for(DestinyCharacter destinyCharacter : this.getCharacters()) {
			if(destinyCharacter.getD2class() == destinyClass) {
				if(toReturn != null) {
					throw new IllegalStateException("There is more than one active character on this account with the same Class type. You will not be able to use this method.");
				}

				toReturn = destinyCharacter;
			}
		}

		return toReturn;
	}

	/**
	 * Adds up all the time played across all characters in minutes
	 *
	 * @return The time played of this user, in minutes
	 */
	public int getTimePlayed() throws APIException {
		if (playTime != -1) { return playTime; }
		for (DestinyCharacter c : getCharacters()) {
			playTime += Integer.parseInt(c.getMinutesPlayedTotal());
		}
		return playTime;
	}

	/**
	 * Gets the clan that this user is a member of.
	 */
	public Clan getClan() throws APIException {
		if (clan != null) { return clan; }

		JsonObject jo2 = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/User/" + getMembershipType() + "/" + getID() + "/0/1/?components=200").getAsJsonObject("Response");
		clan = new Clan(jo2.get("results").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("group").get("groupId").getAsLong());

		return clan;
	}

	/**
	 * If the user is currently in a clan
	 *
	 * @return Returns true if the user is a member of a clan
	 */
	public boolean isAMemberOfAClan() throws APIException {
		return getClan() != null;
	}

	/**
	 * An oauth request as this user to allow or disallow clan invites
	 * Not currently working
	 */
	@Deprecated
	public void allowClanInvites(boolean allowInvites) {

	}

	/**
	 * Determines if the user is online by comparing the last time they logged off and the number of minutes in their current game session
	 *
	 * @return Returns true if the user is online
	 */
	public boolean isOnline() throws APIException {
		for(DestinyCharacter destinyCharacter : getCharacters()) {
			if(destinyCharacter.getLastPlayed().getTime() + ((long) destinyCharacter.getMinutesPlayedThisSession() * 60 * 1000) + (10 * 1000) > System.currentTimeMillis()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return a lot of information pertaining to the activity the user is currently in
	 * E.g. If I am on the tower it will provide information about the ActivityMode and Icons related to it
	 *
	 * @return Null if no information about the current activity is found
	 */
	public ActivityInfo getCurrentActivityInfo() throws APIException {
		JsonObject data = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + getID() + "/?components=204").getAsJsonObject("Response").getAsJsonObject("characterActivities").getAsJsonObject("data");

		for(DestinyCharacter character : getCharacters()) {
			if(data.has(character.getCharacterID())) {
				String hash = data.getAsJsonObject(character.getCharacterID()).get("currentActivityHash").getAsString();
				if(!hash.equals("0")) {
					return new ActivityInfo(hash);
				}
			}
		}

		return null;
	}

	/**
	 * Request to join the specified clan, requires OAuth enabled for current user
	 */
	public void requestToJoinClan(Clan clan) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Apply/" + getMembershipType() + "/");
	}

	/**
	 * Get the Destiny profile object to be used to pull most data.
	 * Uses the preferred platform profile if it has been declared or
	 * it will select the first profile in the profiles array.
	 */
	public JsonObject getJE() throws APIException {
		if (je == null) {
			if (intendedPlatform != -2) {
				for (JsonElement jsonElement : getJO().getAsJsonArray("profiles")) {
					if (jsonElement.getAsJsonObject().get("membershipType").getAsInt() == intendedPlatform) {
						je = jsonElement.getAsJsonObject();
						return je;
					}
				}

				// Some users may have cross saved a console account to their pc account
				for(JsonElement jsonElement : getJO().getAsJsonArray("profilesWithErrors")) {
					if(jsonElement.getAsJsonObject().getAsJsonObject("infoCard").get("membershipType").getAsInt() == intendedPlatform) {
						JsonObject temp = jsonElement.getAsJsonObject().getAsJsonObject("infoCard");
						displayName = temp.get("displayName").getAsString();
						isPublic = temp.get("isPublic").getAsBoolean();
						crossSaveOverride = temp.get("crossSaveOverride").getAsInt();

						// If a user has a profileWithErrors we have to get most info from a profile that is not in error
						for(JsonElement jsonElement1 : getJO().getAsJsonArray("profiles")) {
							// Does the main profile have the intended platform in its applicablemembershipTypes?
							for(JsonElement jsonElement2 : jsonElement1.getAsJsonObject().getAsJsonArray("applicableMembershipTypes")) {
								if(jsonElement2.getAsInt() == intendedPlatform) {
									je = jsonElement1.getAsJsonObject();
									return je;
								}
							}
						}
					}
				}

			} else { // Make sure that the profile has the id for the bungieuser that you want
				for(JsonElement jsonElement : getJO().getAsJsonArray("profiles")) {
					if(jsonElement.getAsJsonObject().get("membershipId").getAsString().equals(getID())) {
						je = jsonElement.getAsJsonObject();
						return je;
					}
				}
			}
			// failsafe
			je = getJO().get("profiles").getAsJsonArray().get(0).getAsJsonObject();
		}


		return je;
	}

	/**
	 * Set the platform that you intend to pull information for, in case the user has multiple profiles.
	 * Deprecated due to a lack of use after the introduction of cross save
	 * @param destinyPlatform The specific platform you want user info for
	 */
	@Deprecated
	public void setIntendedPlatform(DestinyPlatform destinyPlatform) {
		intendedPlatform = destinyPlatform.getPlatformCode();
		je = null;
	}

	public InventoryManager getInventoryManager() {
		if(inventoryManager == null) {
			inventoryManager = new InventoryManager(this);
		}

		return inventoryManager;
	}

	public CollectionsManager getCollectionsManager() throws APIException {
		if (collectionsManager == null) {
			collectionsManager = new CollectionsManager(this);
		}

		return collectionsManager;
	}
}
