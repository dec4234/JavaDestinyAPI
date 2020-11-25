package material.clan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.user.BungieUser;
import material.DestinyAPI;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class Clan {

	private String apiKey = DestinyAPI.getApiKey();
	private HttpUtils hu = new HttpUtils();
	private JsonObject jo; // The entire Clan response
	private JsonObject cjo; // "detail"

	private long clanId;
	private String clanName = null;

	// Details about the clan (more or less in the order they appear in the JSON response)
	private String clanDescription;
	private Date creationDate;
	private int memberCount;
	private boolean isPublic;
	private String motto;

	private boolean allowChat;

	private BungieUser founder;
	private List<BungieUser> admins;
	private List<BungieUser> members = new ArrayList<>();
	private ClanManagement clanManagement;
	private JsonObject jso;
	private JsonArray ja;

	public Clan(long clanId) {
		this.clanId = clanId;
		cjo = hu.urlRequestGET("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200").get("Response").getAsJsonObject().get("detail").getAsJsonObject();
		assignValues();
	}

	public Clan(String clanName) {
		this.clanName = clanName;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/Name/" + clanName.replace(" ", "%20") + "/1/?components=200").get("Response").getAsJsonObject();
		cjo = jo.get("detail").getAsJsonObject();
		assignValues();
	}

	JsonObject jj;

	private void assignValues() {
		if (clanName == null) { // If the clan object was created via ID then the clanName would be null by default
			clanName = cjo.get("name").getAsString();
		} else { // Opposite of previous reason
			clanId = cjo.get("groupId").getAsLong();
		}
		clanDescription = cjo.get("about").getAsString();
		creationDate = StringUtils.valueOfZTime(cjo.get("creationDate").getAsString());
		memberCount = cjo.get("memberCount").getAsInt();
		isPublic = cjo.get("isPublic").getAsBoolean();
		motto = cjo.get("motto").getAsString();
		allowChat = cjo.get("allowChat").getAsBoolean();
		founder = new BungieUser(jo.getAsJsonObject("founder").getAsJsonObject("destinyUserInfo").get("membershipId").getAsString());

		// founder = new BungieUser(jo.get("founder").getAsJsonObject().get("destinyUserInfo").getAsJsonObject().get("membershipId").getAsString());
	}

	public String getClanID() {
		return clanId + "";
	}

	public String getClanName() {
		return clanName;
	}

	public String getClanDescription() { return clanDescription; }

	public Date getCreationDate() { return creationDate; }

	public int getMemberCount() { return memberCount; }

	public boolean isPublic() { return isPublic; }

	public String getMotto() { return motto; }

	public boolean isAllowChat() { return allowChat; }

	public BungieUser getFounder() { return founder; }

	/**
	 * Returns a list of the founder and the admins of the clan
	 * The founder is always the first in this list?
	 * Followed by the admins in the order they were promoted
	 */
	public List<BungieUser> getAdmins() {
		if (admins != null) { return admins; }

		List<BungieUser> temp = new ArrayList<>();
		JsonArray ja = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + clanId + "/AdminsAndFounder/?componenets=200").get("Response").getAsJsonObject().get("results").getAsJsonArray();

		for (JsonElement je : ja) {
			temp.add(new BungieUser(je.getAsJsonObject().get("destinyUserInfo").getAsJsonObject().get("membershipId").getAsString()));
		}

		return temp;
	}

	/**
	 * Returns the average number of days since all members last played
	 */
	public double getAverageInactivityAmongMembers() {
		ArrayList<Double> averages = new ArrayList<Double>();
		int a = 0;
		for(BungieUser bu : this.getMembers()) {
			averages.add(bu.getDaysSinceLastPlayed());
		}
		for(Double d : averages) {
			a += d;
		}
		return a / getMembers().size();
	}

	/**
	 * Returns a list of all members of the clan
	 */
	public List<BungieUser> getMembers() {
		List<BungieUser> temp = new ArrayList<>();

		if(jj == null) {
			jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + clanId + "/Members/").get("Response").getAsJsonObject();
		}

		for(JsonElement je : jj.getAsJsonArray("results")) {
			CompletableFuture<BungieUser> cf = new CompletableFuture<>();

			cf.completeAsync(() -> new BungieUser(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString()));
			try {
				temp.add(cf.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return temp;
	}

	public Date getJoinDate(BungieUser member) {
		if(jj == null) {
			jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + clanId + "/Members/").get("Response").getAsJsonObject();
		}

		List<String> ids = new ArrayList<>();
		for(JsonElement je : jj.getAsJsonArray("results")) {
			if(member.getBungieMembershipID().equals(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString())) {
				return StringUtils.valueOfZTime(je.getAsJsonObject().get("joinDate").getAsString());
			}
		}

		return null; // Return null if there were no matching users found
	}

	public ClanManagement getClanManagement() {
		if (clanManagement != null) { return clanManagement; }
		clanManagement = new ClanManagement(this);
		return clanManagement;
	}
}
