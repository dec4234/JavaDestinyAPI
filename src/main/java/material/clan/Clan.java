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
import com.google.gson.JsonObject;
import material.DestinyAPI;
import material.stats.activities.ActivityMode;
import material.user.BungieUser;
import utils.HttpUtils;
import utils.StringUtils;
import utils.framework.ContentFramework;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Clan extends ContentFramework {

	private String apiKey = DestinyAPI.getApiKey();
	private HttpUtils hu = new HttpUtils();
	private JsonObject jo, cjo; // The entire Clan response

	private long clanId = -1;
	private String clanName, clanDescription, motto;

	// Details about the clan (more or less in the order they appear in the JSON response)
	private Date creationDate;
	private int memberCount = -1;

	private List<BungieUser> admins, members;
	private ClanManagement clanManagement;
	private JsonObject jj;

	public Clan(long clanId) {
		super("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200", source -> {
			return source.getAsJsonObject("Response");
		});
		this.clanId = clanId;
	}

	public Clan(String clanName) {
		super(("https://www.bungie.net/Platform/GroupV2/Name/" + clanName.replace(" ", "%20") + "/1/?components=200"), source -> {
			return source.getAsJsonObject("Response");
		});
		this.clanName = clanName;
	}

	public Clan(long clanId, String clanName) {
		super(("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200"), source -> {
			return source.getAsJsonObject("Response");
		});
		this.clanId = clanId;
		this.clanName = clanName;
	}

	public String getClanID() {
		if(clanId == -1) {
			clanId = getDetail().get("groupId").getAsLong();
		}
		return clanId + "";
	}

	public String getClanName() {
		if(clanName == null) {
			getDetail().get("name").getAsString();
		}
		return clanName;
	}

	public String getClanDescription() {
		if(clanDescription == null) {
			clanDescription = getDetail().get("about").getAsString();
		}
		return clanDescription;
	}

	public Date getCreationDate() {
		if(creationDate == null) {
			creationDate = StringUtils.valueOfZTime(getDetail().get("creationDate").getAsString());
		}
		return creationDate;
	}

	public int getMemberCount() {
		if(memberCount == -1) {
			memberCount = getDetail().get("memberCount").getAsInt();
		}
		return memberCount;
	}

	public boolean isPublic() {
		return getDetail().get("isPublic").getAsBoolean();
	}

	public String getMotto() {
		if(motto == null) {
			motto = getDetail().get("motto").getAsString();
		}
		return motto;
	}

	public boolean isAllowChat() {
		return getDetail().get("allowChat").getAsBoolean();
	}

	public BungieUser getFounder() { return new BungieUser(getJO().getAsJsonObject("founder").getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()); }

	/**
	 * Returns a list of the founder and the admins of the clan
	 * The founder is always the first in this list?
	 * Followed by the admins in the order they were promoted
	 */
	public List<BungieUser> getAdmins() {
		if (admins != null) { return admins; }

		List<BungieUser> temp = new ArrayList<>();
		JsonArray ja = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/AdminsAndFounder/?components=200").getAsJsonObject("Response").getAsJsonArray("results");

		for (JsonElement je : ja) {
			temp.add(new BungieUser(je.getAsJsonObject().get("destinyUserInfo").getAsJsonObject().get("membershipId").getAsString()));
		}

		admins = temp; // Cache this information
		return temp;
	}

	/**
	 * Returns the average number of days since all members last played
	 */
	public double getAverageInactivityAmongMembers() {
		ArrayList<Double> averages = new ArrayList<>();
		int a = 0;
		for (BungieUser bu : this.getMembers()) {
			averages.add(bu.getDaysSinceLastPlayed());
		}
		for (Double d : averages) {
			a += d;
		}
		return (double) a / getMembers().size();
	}

	/**
	 * Sort all of the players in the clan by how inactive they are
	 * Most inactive is 1st, next is 2nd, etc.
	 */
	public List<BungieUser> getMostInactiveMembers(int numberOfResults, String... exclude) {
		List<BungieUser> list = getMembers();
		List<String> exlcluded = Arrays.asList(exclude);
		List<BungieUser> sorted = new LinkedList<>();
		BungieUser temp = null;

		for(int i = 0; i < numberOfResults; i++) {
			for (BungieUser bungieUser : list) {
				if (temp != null) {
					if (temp != bungieUser && !sorted.contains(bungieUser) && !exlcluded.contains(bungieUser.getBungieMembershipID())) {
						if (bungieUser.getDaysSinceLastPlayed() > temp.getDaysSinceLastPlayed()) {
							temp = bungieUser;
						}
					}
				} else {
					temp = bungieUser;
				}
			}
			sorted.add(temp);
			temp = null;
		}

		return sorted;
	}

	/**
	 * Search for all of the members in this clan that have the string in their name
	 */
	public List<BungieUser> searchMembers(String name) {
		List<BungieUser> list = new LinkedList<>();

		for (BungieUser bungieUser : getMembers()) {
			if (bungieUser.getDisplayName().contains(name)) {
				list.add(bungieUser);
			}
		}

		return list;
	}

	/**
	 * Newly reworked
	 * Does not cache values anymore, now the developer is responsible for that
	 * Should be faster than before
	 */
	public List<BungieUser> getMembers() {
		List<BungieUser> source = new ArrayList<>();
		List<String> stream = new ArrayList<>();

		if (jj == null) {
			jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/Members/").get("Response").getAsJsonObject();
		}

		for (JsonElement jsonElement : jj.getAsJsonArray("results")) {
			stream.add(jsonElement.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString());
		}

		if (stream.size() < 20) { // If the amount of people to be scanned is too small to benefit from
			return getMembersOld();
		}

		int index = 0; // The index of the stream to start from
		int[] list = splitIntoParts(stream.size(), 15); // A list of integers used to separate the stream
		int listIndex = 0; // The index in the integer array we are currently on

		while (index < stream.size()) { // Until we have completely looped through the stream
			int i = list[listIndex];
			new MemberThread(source, stream.subList(index, index + i)).start();

			index += i;

			listIndex++;
		}

		return source;
	}

	/**
	 * Returns a list of all members of the clan
	 * Now deprecated in favor of getMembers()
	 */
	@Deprecated
	public List<BungieUser> getMembersOld() {
		List<BungieUser> temp = new ArrayList<>();

		if (members != null) {
			return members;
		}

		if (jj == null) {
			jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/Members/").get("Response").getAsJsonObject();
		}

		for (JsonElement je : jj.getAsJsonArray("results")) {
			CompletableFuture<BungieUser> cf = new CompletableFuture<>();

			cf.completeAsync(() -> new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
			try {
				temp.add(cf.get());
			} catch (InterruptedException | ExecutionException e) {
				System.out.println("Returned a null list of users for the clan in Clan.getMembers()");
				return null;
			}
		}
		members = temp;
		return temp;
	}

	/**
	 * Returns if this BungieUser is a member of the clan
	 */
	public boolean isMember(BungieUser bungieUser) {
		return isMember(bungieUser.getBungieMembershipID());
	}

	public boolean isMember(String bungieID) {
		for (BungieUser bungieUser : getMembers()) {
			if (bungieUser.getBungieMembershipID().equals(bungieID)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Retrieve a JsonObject depicting the top stats of the clan
	 * Unfortunately does not say who has those top stats
	 */
	public JsonObject getClanStats(ActivityMode... filter) {
		String queryString = "/?modes=";
		for (ActivityMode activityMode : filter) {
			queryString = queryString.concat(activityMode.getBungieValue() + ",");
		}
		queryString = queryString.substring(0, queryString.length() - 2); // Remove the last comma

		return hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/Stats/AggregateClanStats/" + getClanID() + queryString);
	}

	/**
	 * Get the date that this user joined the clan
	 */
	public Date getJoinDate(BungieUser member) {
		if (jj == null) {
			jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/Members/").get("Response").getAsJsonObject();
		}

		for (JsonElement je : jj.getAsJsonArray("results")) {
			if (member.getBungieMembershipID().equals(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString())) {
				return StringUtils.valueOfZTime(je.getAsJsonObject().get("joinDate").getAsString());
			}
		}

		return null; // Return null if there were no matching users found
	}

	/**
	 * Get the management class for this clan
	 */
	public ClanManagement getClanManagement() {
		if (clanManagement != null) { return clanManagement; }
		clanManagement = new ClanManagement(this);
		return clanManagement;
	}

	// Util Methods / Classes used elsewhere

	private class MemberThread extends Thread {

		public MemberThread(List<BungieUser> source, List<String> list) {
			for (String string : list) {
				source.add(new BungieUser(string));
			}
		}

		public void run() {

		}
	}

	private int[] splitIntoParts(int whole, int parts) {
		int[] arr = new int[parts];
		int remain = whole;
		int partsLeft = parts;
		for (int i = 0; partsLeft > 0; i++) {
			int size = (remain + partsLeft - 1) / partsLeft; // rounded up, aka ceiling
			arr[i] = size;
			remain -= size;
			partsLeft--;
		}
		return arr;
	}

	private JsonObject getDetail() {
		return getJO().getAsJsonObject("detail");
	}
}
