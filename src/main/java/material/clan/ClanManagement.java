/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.clan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import material.user.BungieUser;
import utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Managing clans like kicking/accepting players and accessing sensitive information
 * Requires OAuth to be set up
 * Requires the user who authenticated the app to be an administrator of the clan they are trying to manage
 */
public class ClanManagement {

	HttpUtils hu = new HttpUtils();

	private Clan clan;
	private List<BungieUser> bannedMembers;
	private List<BungieUser> pendingMembers;

	public ClanManagement(Clan clan) {
		this.clan = clan;
	}

	/**
	 * Kicks this user from the clan
	 */
	public void kickPlayer(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/Kick/", "");
	}

	/**
	 * Bans the user from the clan
	 */
	public void banUser(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/Ban/", "");
	}

	/**
	 * Unbans this user from the clan, as long as they are banned, of course
	 */
	public void unbanUser(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/Unban/", "");
	}

	/**
	 * Invites the specified user to join the clan
	 */
	public void inviteUser(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/IndividualInvite/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/", "");
	}

	/**
	 * Cancels the invite for this user to join the clan
	 */
	public void cancelInvite(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/IndividualInviteCancel/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/", "");
	}

	/**
	 * Approves this user's request to join the clan if and only if they have requested to join
	 */
	public void approvePendingMember(BungieUser bungieUser) {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Approve/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/", "");
	}

	/**
	 * Approves all requests to join the clan
	 */
	public void approveAllPendingMembers() {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/ApproveAll/", "");
	}

	/**
	 * Denies all pending requests to join the clan :)
	 */
	public void denyAllPendingMembers() {
		hu.urlRequestPOSTOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/DenyAll/?components=200", "");
	}

	/**
	 * Abdicates foundership to the admin specified (This user must already be an admin of the clan)
	 *
	 * @param bungieUser The user who will be the new founder (leader) of the clan
	 */
	public void abdicateFoundership(BungieUser bungieUser) {
		hu.urlRequestPOST("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Admin/AbdicateFoundership/" + bungieUser.getMembershipType() + "/" + bungieUser.getBungieMembershipID() + "/", "");
	}

	/**
	 * Gets a list of members who have been banned from the clan
	 *
	 * @return The list of banned users
	 */
	public List<BungieUser> getBannedMembers() {
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
	public List<BungieUser> getPendingMembers() {
		List<BungieUser> temp = new ArrayList<>();
		JsonArray ja = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Pending/?components=200").get("Response").getAsJsonObject().get("results").getAsJsonArray();

		for (JsonElement je : ja) {
			temp.add(new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
		}

		return temp;
	}

	/**
	 * Check if a BungieUser is a pending applicant without the performance overhead of creating multiple BungieUsers
	 */
	public boolean isPendingMember(BungieUser bungieUser) {
		JsonArray ja = hu.urlRequestGETOauth("https://www.bungie.net/Platform/GroupV2/" + clan.getClanID() + "/Members/Pending/?components=200").get("Response").getAsJsonObject().get("results").getAsJsonArray();

		for(JsonElement jsonElement : ja) {
			if(bungieUser.getBungieMembershipID().equals(jsonElement.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString())) {
				return true;
			}
		}

		return false;
	}
}
