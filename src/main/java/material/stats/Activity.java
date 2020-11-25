package material.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.user.BungieUser;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An activity in this case is a PGCR (Post Game Carnage Report)
 * It contains data about an activity that happened like a raid or crucible match
 */
public class Activity {

	HttpUtils hu = new HttpUtils();
	String activityId;
	JsonObject jo;

	private Date time;
	private String referenceId, directoryActivityHash, instanceId;
	private int mode;

	public Activity(String activityId) {
		this.activityId = activityId;
		jo = hu.urlRequestGET("https://stats.bungie.net/Platform/Destiny2/Stats/PostGameCarnageReport/" + activityId + "/").getAsJsonObject("Response");
	}

	public Date getDatePlayed() {
		if(time != null) return time;
		time = StringUtils.valueOfZTime(jo.get("period").getAsString());
		return time;
	}

	public String getReferenceId() {
		if(referenceId != null) return referenceId;
		referenceId = jo.getAsJsonObject("activityDetails").get("referenceId").getAsString();
		return referenceId;
	}

	public String getDirectoryActivityHash() {
		if(directoryActivityHash != null) return directoryActivityHash;
		directoryActivityHash = jo.getAsJsonObject("activityDetails").get("directorActivityHash").getAsString();
		return directoryActivityHash;
	}

	/**
	 * Gets the instance id, which happens to be the same as the activityId :)
=	 */
	public String getInstanceId() {
		if(instanceId != null) return instanceId;
		instanceId = jo.get("instanceId").getAsString();
		return instanceId;
	}

	private int getModeNumber() {
		if(mode != 0) return mode;
		mode = jo.getAsJsonObject("activityDetails").get("mode").getAsInt();
		return mode;
	}

	/**
	 * Return the mode of the activity
	 * Mode list on this page
	 * https://bungie-net.github.io/multi/schema_Destiny-Definitions-DestinyActivityDefinition.html
	 */
	public ActivityMode getMode() {
		for(ActivityMode am : ActivityMode.values()) {
			if(am.getBungieValue() == getModeNumber()) {
				return am;
			}
		}
		return null;
	}

	public List<ActivityParticipant> getParticipants() {
		List<ActivityParticipant> temp = new ArrayList<>();
		for(JsonElement je : jo.get("entries").getAsJsonArray()) {
			temp.add(new ActivityParticipant(je.getAsJsonObject()));
		}
		return temp;
	}

	/**
	 * If you need to get data not inside of this class
	 */
	public JsonObject getJsonObject() {
		return jo;
	}

}
