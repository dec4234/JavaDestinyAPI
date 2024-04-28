/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.clan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Managing clans like kicking/accepting players and accessing sensitive information.
 * Requires OAuth to be set up!
 * Requires the user who authenticated the app to be an administrator of the clan they are trying to manage!
 */
public class ClanManagement {

	HttpUtils hu = DestinyAPI.getHttpUtils();

	private Clan clan;
	private List<BungieUser> bannedMembers;
	private List<BungieUser> pendingMembers;

	public ClanManagement(Clan clan) {
		this.clan = clan;
	}

	/**
	 * Kicks this user from the clan
	 */
	public void kickPlayer(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/Kick/");
	}

	/**
	 * Bans the user from the clan
	 */
	public void banUser(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/Ban/");
	}

	/**
	 * Unbans this user from the clan, as long as they are banned, of course
	 */
	public void unbanUser(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/Unban/");
	}

	/**
	 * Invites the specified user to join the clan
	 */
	public void inviteUser(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/IndividualInvite/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/");
	}

	/**
	 * Cancels the invite for this user to join the clan
	 */
	public void cancelInvite(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/IndividualInviteCancel/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/");
	}

	/**
	 * Approves this user's request to join the clan if and only if they have requested to join
	 */
	public void approvePendingMember(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Approve/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/");
	}

	/**
	 * Approves all requests to join the clan
	 */
	public void approveAllPendingMembers() throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/ApproveAll/");
	}

	/**
	 * Denies all pending requests to join the clan :)
	 */
	public void denyAllPendingMembers() throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/DenyAll/?components=200");
	}

	/**
	 * Abdicates foundership to the admin specified (This user must already be an admin of the clan)
	 *
	 * @param bungieUser The user who will be the new founder (leader) of the clan
	 */
	public void abdicateFoundership(BungieUser bungieUser) throws APIException {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Admin/AbdicateFoundership/" + bungieUser.getMembershipType() + "/" + bungieUser.getID() + "/");
	}

	/**
	 * Adds a new optional conversation to the clan
	 *
	 * @param chatName The name of the chat
	 * @param clanChatSecuritySetting The security setting of the chat
	 */
	public void addOptionalConversation(String chatName, ClanChatSecuritySetting clanChatSecuritySetting) throws APIException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("chatName", chatName);
		jsonObject.addProperty("chatSecurity", clanChatSecuritySetting.getSetting());

		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Admin/OptionalConversations/Add/", jsonObject);
	}

	/**
	 * Edits an optional conversation
	 *
	 * @param conversationID The ID of the conversation
	 * @param chatEnabled Whether or not the chat is enabled
	 * @param chatName The name of the chat
	 * @param clanChatSecuritySetting The security setting of the chat
	 */
	public void editOptionalConversation(String conversationID, boolean chatEnabled, String chatName, ClanChatSecuritySetting clanChatSecuritySetting) throws APIException {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("chatName", chatName);
		jsonObject.addProperty("chatSecurity", clanChatSecuritySetting.getSetting());
		jsonObject.addProperty("chatEnabled", chatEnabled);

		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Admin/OptionalConversations/" + conversationID + "/Edit/", jsonObject);
	}

	/**
	 * Gets a list of members who have been banned from the clan
	 *
	 * @return The list of banned users
	 */
	public List<BungieUser> getBannedMembers() throws APIException {
		if (bannedMembers != null) { return bannedMembers; }

		List<BungieUser> temp = new ArrayList<>();

		JsonArray jo = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Banned/?componenets=200").getAsJsonObject("Response").get("results").getAsJsonArray();
		for (JsonElement je : jo) {
			temp.add(new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
		}
		return temp;
	}

	/**
	 * Returns a list of pending members to the clan, Never cached: always makes a new request
	 */
	public List<BungieUser> getPendingMembers() throws APIException {
		List<BungieUser> temp = new ArrayList<>();
		JsonArray ja = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Pending/?components=200").get("Response").getAsJsonObject().get("results").getAsJsonArray();

		for (JsonElement je : ja) {
			temp.add(new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
		}

		return temp;
	}

	/**
	 * Returns a list of members who have been invited to the clan
	 *
	 * Has pagination feature that is not implemented because what clan would have more than 50 invited users?
	 *
	 * @return The list of invited users
	 */
	public List<BungieUser> getInvitedMembers() throws APIException {
		List<BungieUser> temp = new ArrayList<>();

		JsonArray ja = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/InvitedIndividuals/?components=200").getAsJsonObject("Response").getAsJsonArray("results");

		for(JsonElement je : ja) {
			temp.add(new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
		}

		return temp;
	}

	/**
	 * Check if a BungieUser is a pending applicant without the performance overhead of creating multiple BungieUsers
	 */
	public boolean isPendingMember(BungieUser bungieUser) throws APIException {
		JsonArray ja = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Pending/?components=200").get("Response").getAsJsonObject().get("results").getAsJsonArray();

		for(JsonElement jsonElement : ja) {
			if(bungieUser.getID().equals(jsonElement.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString())) {
				return true;
			}
		}

		return false;
	}
}
