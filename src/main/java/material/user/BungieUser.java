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
import material.clan.Clan;
import material.stats.activities.ActivityInfo;
import utils.HttpUtils;
import utils.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BungieUser {

	private String bungieMembershipID;
	private String displayName;
	private Date lastPlayed;
	private JsonObject jo, je; // The JsonObject containing all data related to the user
	private HttpUtils hu = new HttpUtils();

	private ArrayList<Integer> applicableMembershipTypes = new ArrayList<>();

	private List<DestinyCharacter> destinyCharacters = null;
	private int playTime = -1, crossSaveOverride = -1, membershipType = -1;
	private boolean isPublic, isCrossSavePrimary, isOverridden = false;

	private Clan clan;

	public BungieUser(String bungieMembershipID) {
		this.bungieMembershipID = bungieMembershipID;
	}

	public BungieUser(String bungieMembershipID, String displayName, int crossSaveOverride, int membershipType, boolean isPublic) {
		this.bungieMembershipID = bungieMembershipID;
		this.displayName = displayName;
		this.crossSaveOverride = crossSaveOverride;
		this.membershipType = membershipType;
		this.isPublic = isPublic;
	}

	/**
	 * Determines if the user has any profiles on their account
	 * Useful to see if a user's account has any data on it
	 */
	public boolean isValidUser() {
		try {
			checkJO();
			return jo.get("Response").getAsJsonObject().get("profiles").getAsJsonArray().size() != 0;
		} catch (NullPointerException nullPointerException) {
			return false;
		}
	}

	/**
	 * Gets the bungie membership ID of the user
	 */
	public String getBungieMembershipID() { return bungieMembershipID; }

	/**
	 * Gets the display name of the user
	 * Prefers returning the name of their account on steam, if they have one
	 */
	public String getDisplayName() {
		if (displayName == null) {
			checkJE();
			displayName = je.get("displayName").getAsString();
			JsonArray array = jo.getAsJsonObject("Response").getAsJsonArray("profilesWithErrors");
			if (array != null && array.size() != 0 && je.get("membershipType").getAsInt() != 3) {
				for (JsonElement je : array) {
					if (je.getAsJsonObject().getAsJsonObject("infoCard").get("membershipType").getAsInt() == 3) {
						checkJE();
						displayName = je.getAsJsonObject().getAsJsonObject("infoCard").get("displayName").getAsString();
					}
				}
			}
		}

		return displayName;
	}

	/**
	 * Gets the last day this user was seen online
	 */
	public Date getLastPlayed() {
		if (lastPlayed == null) {
			checkJE();
			lastPlayed = StringUtils.valueOfZTime(je.get("dateLastPlayed").getAsString());
		}
		return lastPlayed;
	}

	/**
	 * Gets an integer representing the number of days since the user last played
	 */
	public double getDaysSinceLastPlayed() {
		return (double) ChronoUnit.DAYS.between(getLastPlayed().toInstant(), new Date().toInstant());
	}

	public boolean isOverridden() {
		checkJE();
		return !isOverridden ? isOverridden = je.get("isOverridden").getAsBoolean() : isOverridden;
	}

	public boolean isCrossSavePrimary() {
		checkJE();
		return !isCrossSavePrimary ? isCrossSavePrimary = je.get("isCrossSavePrimary").getAsBoolean() : isCrossSavePrimary;
	}

	public int getCrossSaveOverride() {
		checkJE();
		return crossSaveOverride == -1 ? crossSaveOverride = je.get("crossSaveOverride").getAsInt() : crossSaveOverride;
	}

	public ArrayList<Integer> getApplicableMembershipTypes() {
		if (applicableMembershipTypes.isEmpty()) {
			checkJE();
			applicableMembershipTypes.clear();
			for (JsonElement jj : je.get("applicableMembershipTypes").getAsJsonArray()) {
				applicableMembershipTypes.add(jj.getAsInt());
			}
		}
		return applicableMembershipTypes;
	}

	public boolean isPublic() {
		checkJE();
		return !isPublic ? isPublic = je.get("isPublic").getAsBoolean() : isPublic;
	}

	/**
	 * Returns the membership type
	 */
	public int getMembershipType() {
		checkJE();
		return membershipType == -1 ? membershipType = je.get("membershipType").getAsInt() : membershipType;
	}

	/**
	 * Gets the characters that are attached to this bungieuser
	 */
	public List<DestinyCharacter> getCharacters() {
		if (destinyCharacters != null) { return destinyCharacters; }
		destinyCharacters = new ArrayList<>();
		JsonArray ja = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieMembershipID + "/?components=100").getAsJsonObject("Response").getAsJsonObject("profile").getAsJsonObject("data").getAsJsonArray("characterIds");
		if (ja == null || ja.size() == 0) {
			return null;
		}

		for (JsonElement je : ja) {
			destinyCharacters.add(new DestinyCharacter(this, je.getAsString()));
		}

		return destinyCharacters;
	}

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

		JsonObject jo = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/User/" + getMembershipType() + "/" + getBungieMembershipID() + "/0/1/?components=200").getAsJsonObject("Response");
		clan = new Clan(jo.get("results").getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("group").get("groupId").getAsLong());
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
	 * @return Null if no information about the current activity is found
	 */
	public ActivityInfo getCurrentActivityInfo() {
		JsonObject data = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + getBungieMembershipID() + "/?components=204").getAsJsonObject("Response").getAsJsonObject("characterActivities").getAsJsonObject("data");

		for(DestinyCharacter character : getCharacters()) {
			if(data.has(character.getCharacterID())) {
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

	public JsonObject getJsonObject() {
		checkJO();
		return jo;
	}

	public void checkJO() {
		if (jo == null) {
			jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200");
		}
	}

	public void checkJE() {
		checkJO();
		if (je == null) {
			je = jo.get("Response").getAsJsonObject().get("profiles").getAsJsonArray().get(0).getAsJsonObject();
		}
	}
}
