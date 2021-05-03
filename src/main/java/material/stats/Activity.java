/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.stats;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.HttpUtils;
import utils.StringUtils;
import utils.framework.ContentFramework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An activity in this case is a PGCR (Post Game Carnage Report)
 * It contains data about an activity that happened like a raid or crucible match
 *
 * Can not retrieve info about Activities that have not ended yet
 */
public class Activity extends ContentFramework {

	private HttpUtils hu = new HttpUtils();

	private Date time;
	private String activityId, referenceId, directoryActivityHash, instanceId;
	private int mode = -1;
	private int[] modes;

	public Activity(String activityId) {
		super("https://stats.bungie.net/Platform/Destiny2/Stats/PostGameCarnageReport/" + activityId + "/", source -> {
			return source.getAsJsonObject("Response");
		});
		this.activityId = activityId;
	}

	/**
	 * Initialize an activity with more information which could improve load times
	 */
	public Activity(String activityId,  String referenceId, String directoryActivityHash, String rawDate, int mode, int[] modes) {
		super("https://stats.bungie.net/Platform/Destiny2/Stats/PostGameCarnageReport/" + activityId + "/", source -> {
			return source.getAsJsonObject("Response");
		});
		this.activityId = activityId;
		this.referenceId = referenceId;
		this.directoryActivityHash = directoryActivityHash;
		this.time = StringUtils.valueOfZTime(rawDate);
		this.mode = mode;
		this.modes = modes;
	}

	/**
	 * Get the date this Activity was played
	 * @return
	 */
	public Date getDatePlayed() {
		checkJO();
		return time == null ? time = StringUtils.valueOfZTime(getJO().get("period").getAsString()) : time;
	}

	/**
	 * Get the referenceID of this activity
	 * @return
	 */
	public String getReferenceId() {
		checkJO();
		return referenceId == null ? referenceId = getJO().getAsJsonObject("activityDetails").get("referenceId").getAsString() : referenceId;
	}

	/**
	 * Get the director activity hash (the type of activity played?)
	 */
	public String getDirectoryActivityHash() {
		checkJO();
		return directoryActivityHash == null ? directoryActivityHash = getJO().getAsJsonObject("activityDetails").get("directorActivityHash").getAsString() : directoryActivityHash;
	}

	/**
	 * Gets the instance id, which happens to be the same as the activityId :)
=	 */
	public String getInstanceId() {
		checkJO();
		return instanceId == null ? instanceId = getJO().get("instanceId").getAsString() : instanceId;
	}

	/**
	 * Get the mode number of the Activity
	 */
	private int getModeNumber() {
		checkJO();
		return mode == -1 ? mode = getJO().getAsJsonObject("activityDetails").get("mode").getAsInt() : mode;
	}

	/**
	 * Return the mode of the activity
	 * Mode list on this page
	 * https://bungie-net.github.io/multi/schema_Destiny-Definitions-DestinyActivityDefinition.html
	 */
	public ActivityMode getMode() {
		checkJO();
		for(ActivityMode am : ActivityMode.values()) {
			if(am.getBungieValue() == getModeNumber()) {
				return am;
			}
		}
		return null;
	}

	/**
	 * Get all of the participants of this activity
	 * @return
	 */
	public List<ActivityParticipant> getParticipants() {
		checkJO();

		List<ActivityParticipant> temp = new ArrayList<>();
		for(JsonElement je : getJO().get("entries").getAsJsonArray()) {
			temp.add(new ActivityParticipant(je.getAsJsonObject()));
		}
		return temp;
	}

	/**
	 * If you need to get data not inside of this class
	 */
	public JsonObject getJsonObject() {
		checkJO();
		return getJO();
	}
}
