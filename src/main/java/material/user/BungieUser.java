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
import material.DestinyAPI;
import material.clan.Clan;
import material.inventory.CollectionsManager;
import material.inventory.InventoryManager;
import material.stats.activities.ActivityInfo;
import utils.HttpUtils;
import utils.StringUtils;
import utils.framework.ContentFramework;

import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
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
	private int intendedPlatform = -2;
	private InventoryManager inventoryManager;
	private CollectionsManager collectionsManager;

	private Clan clan;

	/**
	 * The simplest starter of a BungieUser.
	 * Just provide the bungie membership id of the user
	 *
	 * @param bungieMembershipID The bungie id of the user you want to get information for
	 */
	public BungieUser(String bungieMembershipID) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
	}

	/**
	 * Used to provide a little more context for what profile the BungieUser class should pull info from
	 * because a LinkedProfiles request can have multiple platform accounts attached
	 * You are responsible for checking the getMembershipTypes() to see if that user has a profile on that platform
	 *
	 * @param bungieMembershipID The bungie Id of the user you want to get information for
	 * @param intendedPlatform   The platform that you want to pull information for
	 */
	public BungieUser(String bungieMembershipID, DestinyPlatform intendedPlatform) {
		super("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieMembershipID = bungieMembershipID;
		this.intendedPlatform = intendedPlatform.getPlatformCode();
	}

	/**
	 * A constructor contianing more information which may decrease load times in certain circumstances
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
	 * Gets the bungie membership ID of the user
	 */
	public String getBungieMembershipID() { return bungieMembershipID; }

	/**
	 * Determines if the user has any profiles on their account
	 * Useful to see if a user's account has any data on it
	 */
	public boolean isValidUser() {
		try {
			return getJO().getAsJsonArray("profiles").size() != 0;
		} catch (NullPointerException nullPointerException) {
			return false;
		}
	}

	/**
	 * Gets the display name of the user
	 * Prefers returning the name of their account on steam, if they have one
	 */
	@Deprecated
	public String getDisplayName() {
		getJE();
		if (displayName == null) {
			displayName = getJE().get("displayName").getAsString();
		}
		return displayName;
	}

	/**
	 * Returns the Global Display Name of the user across all Destiny Platforms
	 * Should be used instead of getDisplayName()
	 *
	 * Please note, an empty string will be returned if a user has not logged in since the
	 * start of Season of The Lost
	 */
	public String getGlobalDisplayName() {
		if(globalDisplayName == null) {

			// LinkedProfiles is not populated with bungieGlobalDisplayName as of 8/29/2021: github issue #1511
			// As far as I know, getSupplementalDisplayName is also the bungieGlobalDisplayName
			globalDisplayName = getJE().get("bungieGlobalDisplayName").getAsString();
		}

		return globalDisplayName;
	}

	/**
	 * Returns the combined displayname and user discriminator as used in friend requests and user searches
	 *
	 * E.g. dec4234#9904
	 */
	public String getSupplementalDisplayName() {
		if(supplementalDisplayName == null) {
			supplementalDisplayName = getJO().getAsJsonObject("bnetMembership").get("supplementalDisplayName").getAsString();
		}

		return supplementalDisplayName;
	}

	/**
	 * Get the discriminator of a user's name
	 *
	 * E.g. "9904" of dec4234#9904
	 */
	public String getDiscriminator() {
		if(discriminator == null) {
			discriminator = getSupplementalDisplayName().split("#")[1];
		}

		return discriminator;
	}

	/**
	 * Gets the last day this user was seen online
	 */
	public Date getLastPlayed() {
		if (lastPlayed == null) {
			lastPlayed = StringUtils.valueOfZTime(getJE().get("dateLastPlayed").getAsString());
		}

		return lastPlayed;
	}

	/**
	 * Gets a double representing the number of days since the user last played
	 */
	public double getDaysSinceLastPlayed() {
		DecimalFormat df = new DecimalFormat("0.##");
		return Double.parseDouble(df.format((new Date().getTime() - getLastPlayed().getTime()) / 1000.0 / 60.0 / 60.0 / 24.0));
	}

	public boolean isOverridden() {
		return !isOverridden ? isOverridden = getJE().get("isOverridden").getAsBoolean() : isOverridden;
	}

	public boolean isCrossSavePrimary() {
		return !isCrossSavePrimary ? isCrossSavePrimary = getJE().get("isCrossSavePrimary").getAsBoolean() : isCrossSavePrimary;
	}

	public int getCrossSaveOverride() {
		return crossSaveOverride == -1 ? crossSaveOverride = getJE().get("crossSaveOverride").getAsInt() : crossSaveOverride;
	}

	/**
	 * Get the applicable membership types declared in the response
	 */
	public ArrayList<Integer> getApplicableMembershipTypes() {
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
	public ArrayList<Integer> getMembershipTypes() {
		ArrayList<Integer> integers = new ArrayList<>();

		for (JsonElement jsonElement : getJO().get("profiles").getAsJsonArray()) {
			integers.add(jsonElement.getAsJsonObject().get("membershipType").getAsInt());
		}

		return integers;
	}

	public boolean isPublic() {
		return !isPublic ? isPublic = getJE().get("isPublic").getAsBoolean() : isPublic;
	}

	/**
	 * Returns the membership type of the main profile
	 */
	public int getMembershipType() {
		return membershipType == -1 ? membershipType = getJE().get("membershipType").getAsInt() : membershipType;
	}

	/**
	 * Gets the characters that are attached to this BungieUser
	 *
	 * @return The list of characters associated with this account, or null if none are found
	 */
	public List<DestinyCharacter> getCharacters() {
		if (destinyCharacters != null) { return destinyCharacters; }
		destinyCharacters = new ArrayList<>();
		JsonArray ja = null;
		try {
			ja = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieMembershipID + "/?components=100").getAsJsonObject("Response").getAsJsonObject("profile").getAsJsonObject("data").getAsJsonArray("characterIds");
		} catch (NullPointerException e) {
			return destinyCharacters;
		}
		if (ja == null || ja.size() == 0) {
			return null;
		}

		for (JsonElement je : ja) {
			destinyCharacters.add(new DestinyCharacter(this, je.getAsString()));
		}

		return destinyCharacters;
	}

	/**
	 * Adds up all of the time played across all characters
	 *
	 * @return The time played of this user, in minutes
	 */
	public int getTimePlayed() {
		if (playTime != -1) { return playTime; }
		for (DestinyCharacter c : getCharacters()) {
			playTime += Integer.parseInt(c.getMinutesPlayedTotal());
		}
		return playTime;
	}

	/**
	 * Gets the clan that this user is a member of
	 */
	public Clan getClan() {
		if (clan != null) { return clan; }

		JsonObject jo2 = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/User/" + getMembershipType() + "/" + getBungieMembershipID() + "/0/1/?components=200").getAsJsonObject("Response");
		clan = new Clan(jo2.get("results").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("group").get("groupId").getAsLong());
		return clan;
	}

	/**
	 * An oauth request as this user to allow or disallow clan invites
	 * Not currently working
	 */
	@Deprecated
	public void allowClanInvites(boolean allowInvites) {

	}

	/**
	 * Return a lot of information pertaining to the activity the user is currently in
	 * E.g. If I am on the tower it will provide information about the ActivityMode and Icons related to it
	 *
	 * @return Null if no information about the current activity is found
	 */
	public ActivityInfo getCurrentActivityInfo() {
		JsonObject data = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + getBungieMembershipID() + "/?components=204").getAsJsonObject("Response").getAsJsonObject("characterActivities").getAsJsonObject("data");

		for (DestinyCharacter character : getCharacters()) {
			if (data.has(character.getCharacterID())) {
				String hash = data.getAsJsonObject(character.getCharacterID()).get("currentActivityHash").getAsString();
				if (!hash.equals("0")) {
					return new ActivityInfo(hash);
				}
			}
		}

		return null;
	}

	/**
	 * Request to join the specified clan
	 */
	public void requestToJoinClan(Clan clan) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Apply/" + getMembershipType() + "/", "");
	}

	/**
	 * Get the Destiny profile object to be used to pull most data
	 * Uses the preferred platform profile if it has been declared or
	 * it will select the first profile in the profiles array
	 */
	public JsonObject getJE() {
		if(je == null) {
			if (intendedPlatform != -2) {
				for (JsonElement jsonElement : getJO().getAsJsonArray("profiles")) {
					if (jsonElement.getAsJsonObject().get("membershipType").getAsInt() == intendedPlatform) {
						je = jsonElement.getAsJsonObject();
						return je;
					}
				}

				// Some users may have cross saved a console account to their pc account
				for (JsonElement jsonElement : getJO().getAsJsonArray("profilesWithErrors")) {
					if (jsonElement.getAsJsonObject().getAsJsonObject("infoCard").get("membershipType").getAsInt() == intendedPlatform) {
						JsonObject temp = jsonElement.getAsJsonObject().getAsJsonObject("infoCard");
						displayName = temp.get("displayName").getAsString();
						isPublic = temp.get("isPublic").getAsBoolean();
						crossSaveOverride = temp.get("crossSaveOverride").getAsInt();

						// If a user has a profileWithErrors we have to get most info from a profile that is not in error
						for (JsonElement jsonElement1 : getJO().getAsJsonArray("profiles")) {
							// Does the main profile have the intended platform in its applicablemembershipTypes?
							for (JsonElement jsonElement2 : jsonElement1.getAsJsonObject().getAsJsonArray("applicableMembershipTypes")) {
								if (jsonElement2.getAsInt() == intendedPlatform) {
									je = jsonElement1.getAsJsonObject();
									return je;
								}
							}
						}
					}
				}

			}
			je = getJO().get("profiles").getAsJsonArray().get(0).getAsJsonObject();
		}


		return je;
	}

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

	public CollectionsManager getCollectionsManager() {
		if(collectionsManager == null) {
			collectionsManager = new CollectionsManager(this);
		}

		return collectionsManager;
	}
}
