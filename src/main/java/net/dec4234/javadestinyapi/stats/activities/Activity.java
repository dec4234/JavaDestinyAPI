/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.stats.activities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.StringUtils;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An activity in this case is a PGCR (Post Game Carnage Report)
 * It contains data about an activity that happened like a raid or crucible match
 * Can not retrieve info about Activities that have not ended yet
 */
public class Activity extends ContentFramework {

	private HttpUtils hu = DestinyAPI.getHttpUtils();

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

	public Activity(String activityId, Date dateOccured) {
		super("https://stats.bungie.net/Platform/Destiny2/Stats/PostGameCarnageReport/" + activityId + "/", source -> {
			return source.getAsJsonObject("Response");
		});
		this.activityId = activityId;
		this.time = dateOccured;
	}

	/**
	 * Initialize an activity with more information which could improve load times
	 */
	public Activity(String activityId, String referenceId, String directoryActivityHash, String rawDate, int mode, int[] modes) {
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
	 *
	 * @return
	 */
	public Date getDatePlayed() throws APIException {
		checkJO();
		return time == null ? time = StringUtils.valueOfZTime(getJO().get("period").getAsString()) : time;
	}

	/**
	 * Get the referenceID of this activity
	 *
	 * @return
	 */
	public String getReferenceId() throws APIException {
		checkJO();
		return referenceId == null ? referenceId = getJO().getAsJsonObject("activityDetails").get("referenceId").getAsString() : referenceId;
	}

	/**
	 * Get the director activity hash (the type of activity played?)
	 */
	public String getDirectoryActivityHash() throws APIException {
		checkJO();
		return directoryActivityHash == null ? directoryActivityHash = getJO().getAsJsonObject("activityDetails").get("directorActivityHash").getAsString() : directoryActivityHash;
	}

	/**
	 * Gets the instance id, which happens to be the same as the activityId :)
	 * =
	 */
	public String getInstanceId() throws APIException {
		checkJO();
		return activityId == null ? activityId = getJO().get("instanceId").getAsString() : activityId;
	}

	/**
	 * Get the mode number of the Activity
	 */
	private int getModeNumber() throws APIException {
		checkJO();
		return mode == -1 ? mode = getJO().getAsJsonObject("activityDetails").get("mode").getAsInt() : mode;
	}

	/**
	 * Return the mode of the activity
	 * Mode list on this page
	 * https://bungie-net.github.io/multi/schema_Destiny-Definitions-DestinyActivityDefinition.html
	 */
	public ActivityMode getMode() throws APIException {
		checkJO();
		for (ActivityMode am : ActivityMode.values()) {
			if (am.getBungieValue() == getModeNumber()) {
				return am;
			}
		}
		return null;
	}

	/**
	 * Get all of the participants of this activity
	 *
	 * @return
	 */
	public List<ActivityParticipant> getParticipants() throws APIException {

		List<ActivityParticipant> temp = new ArrayList<>();
		try {
			for (JsonElement je : getJsonObject().get("entries").getAsJsonArray()) {
				temp.add(new ActivityParticipant(je.getAsJsonObject()));
			}
		} catch (NullPointerException e) {
			System.out.println(getJO());
		}

		return temp;
	}

	/**
	 * If you need to get data not inside of this class
	 */
	public JsonObject getJsonObject() throws APIException {
		checkJO();
		return getJO();
	}
}
