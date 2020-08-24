package material.clan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.user.BungieUser;
import material.DestinyAPI;
import utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	private void assignValues() {
		if (clanName == null) { // If the clan object was created via ID then the clanName would be null by default
			clanName = cjo.get("name").getAsString();
		} else { // Opposite of previous reason
			clanId = cjo.get("groupId").getAsLong();
		}
		// creationDate = StringUtils.valueOfZTime(cjo.get("creationDate").getAsString()); // The date the clan was created
		clanDescription = cjo.get("about").getAsString();
		memberCount = cjo.get("memberCount").getAsInt();
		isPublic = cjo.get("isPublic").getAsBoolean();
		motto = cjo.get("motto").getAsString();
		allowChat = cjo.get("allowChat").getAsBoolean();

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

	public List<BungieUser> getMembers() {
		if (!members.isEmpty()) { return members; }

		jso = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + clanId + "/Members/").get("Response").getAsJsonObject();

		for (JsonElement je : jso.get("results").getAsJsonArray()) {
			members.add(new BungieUser(je.getAsJsonObject().get("destinyUserInfo").getAsJsonObject().get("membershipId").getAsString()));
		}

		return members;
	}

	public ClanManagement getClanManagement() {
		if (clanManagement != null) { return clanManagement; }
		clanManagement = new ClanManagement(this);
		return clanManagement;
	}
}
