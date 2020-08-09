package material.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.clan.Clan;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BungieUser {

	private boolean isValidUser;

	private String bungieMembershipID;
	private String displayName;
	private Date lastPlayed;
	private JsonObject jo; // The JsonObject containing all data related to the user
	private HttpUtils hu = new HttpUtils();

	private boolean isOverriden;
	private boolean isCrossSavePrimary;
	private int crossSaveOverride;
	private ArrayList<Integer> applicableMembershipTypes = new ArrayList<>();

	private boolean isPublic;
	private int membershipType;
	private List<Character> characters;

	private String iconPath;
	private Clan clan;

	public BungieUser(String bungieMembershipID) {
		this.bungieMembershipID = bungieMembershipID;

		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/3/Profile/" + bungieMembershipID + "/LinkedProfiles/?components=200");
		assignValues();
	}

	private void assignValues() {
		JsonObject je;
		if(jo.get("Response").getAsJsonObject().get("profiles").getAsJsonArray().size() == 0) {
			isValidUser = false;
			return;
		}

		isValidUser = true;
		je = jo.get("Response").getAsJsonObject().get("profiles").getAsJsonArray().get(0).getAsJsonObject();

		displayName = je.get("displayName").getAsString();
		lastPlayed = StringUtils.valueOfZTime(je.get("dateLastPlayed").getAsString());

		isOverriden = je.get("isOverridden").getAsBoolean();
		isCrossSavePrimary = je.get("isCrossSavePrimary").getAsBoolean();
		crossSaveOverride = je.get("crossSaveOverride").getAsInt();

		applicableMembershipTypes.clear();
		for(JsonElement jj : je.get("applicableMembershipTypes").getAsJsonArray()) {
			applicableMembershipTypes.add(jj.getAsInt());
		}

		isPublic = je.get("isPublic").getAsBoolean();
		membershipType = je.get("membershipType").getAsInt();
		// iconPath = jo.get("Response").getAsJsonObject().get("bnetMembership").getAsJsonObject().get("iconPath").getAsString();

	}

	/**
	 * Determines if the user has any profiles on their account
	 * Useful to see if a user's account has any data on it
	 */
	public boolean isValidUser() { return isValidUser; }
	/**
	 * Gets the bungie membership ID of the user
	 */
	public String getBungieMembershipID() { return bungieMembershipID; }
	/**
	 * Gets the display name of the user
	 */
	public String getDisplayName() { return displayName; }
	/**
	 * Gets the last day this user was seen online
	 */
	public Date getLastPlayed() { return lastPlayed; }
	/**
	 * Gets an integer representing the number of days since the user last played
	 */
	public int getDaysSinceLastPlayed() { return (int) (new Date().getTime() - getLastPlayed().getTime()) / 86400000; }

	public boolean isOverriden() { return isOverriden; }
	public boolean isCrossSavePrimary() { return isCrossSavePrimary; }
	public int getCrossSaveOverride() { return crossSaveOverride; }
	public ArrayList<Integer> getApplicableMembershipTypes() { return applicableMembershipTypes; }

	public boolean isPublic() { return isPublic; }
	/**
	 * Returns the membership type
	 */
	public int getMembershipType() { return membershipType; }

	public String getIconPath() { return iconPath; }

	/**
	 * Currently not working
	 */
	@Deprecated
	public List<Character> getCharacters() {
		if(clan != null) return characters;
		characters = new ArrayList<>();
		JsonObject jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + getMembershipType() + "/Profile/" + bungieMembershipID + "/?components=200").getAsJsonObject("Response").getAsJsonObject("characters");


		return characters;
	}

	/**
	 * Gets the clan that this user is a member of
	 */
	public Clan getClan() {
		if(clan != null) return clan;

		JsonObject jo = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/User/" + getMembershipType() + "/" + getBungieMembershipID() + "/0/1/?components=200").get("Response").getAsJsonObject();
		clan = new Clan(jo.get("results").getAsJsonArray().get(0).getAsJsonObject().get("group").getAsJsonObject().get("groupId").getAsLong());
		return clan;
	}

	public void allowClanInvites(boolean allowInvites) {

	}

	/**
	 * Request to join the specified clan
	 */
	public void requestToJoinClan(Clan clan) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Apply/" + getMembershipType() + "/", "");
	}
}
