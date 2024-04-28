/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.stats.activities;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

public class ActivityInfo extends ContentFramework {

	private String name, hash, completionUnlockHash, destinationHash, placeHash, activityTypeHash, activityModeHash, description, icon, pgcrImage, releaseIcon;
	private int modeType, activityModeCategory, releaseTime, activityLightLevel, tier, minParty, maxParty, maxPlayers;
	private ActivityMode activityMode;
	private boolean hasIcon, isPlaylist, inheritFromFreeRoam, suppressOtherRewards, isMatchmade, requiresGuardianOath, isPvP;

	public ActivityInfo(String hash) throws APIException {
		super(ManifestEntityTypes.ACTIVITY, hash, source -> {
			return source.getAsJsonObject("Response");
		});

		// Intialize values of the Activity Mode
		this.name = getDisplayProperties().get("name").getAsString();
		this.description = getDisplayProperties().get("description").getAsString();

		this.hasIcon = getDisplayProperties().get("hasIcon").getAsBoolean();

		if(hasIcon) {
			icon = getDisplayProperties().get("icon").getAsString();
		}

		// this.pgcrImage = getJO().get("pgcrImage").getAsString();
		this.releaseIcon = getJO().get("releaseIcon").getAsString();

		// Hashes
		this.hash = hash;
		this.completionUnlockHash = getJO().get("completionUnlockHash").getAsString();
		this.destinationHash = getJO().get("destinationHash").getAsString();
		this.placeHash = getJO().get("placeHash").getAsString();
		this.activityTypeHash = getJO().get("activityTypeHash").getAsString();
		// this.activityModeHash = getJO().get("activityModeHash").getAsString();

		// Numbers
		if(getJO().has("directActivityModeType")) {
			this.modeType = getJO().get("directActivityModeType").getAsInt();
			this.activityMode = ActivityMode.fromBungieValue(modeType);
		}

		if(getJO().has("activityModeCategory")) {
			this.activityModeCategory = getJO().get("activityModeCategory").getAsInt();
		}
		this.releaseTime = getJO().get("releaseTime").getAsInt();
		this.tier = getJO().get("tier").getAsInt();
		this.activityLightLevel = getJO().get("activityLightLevel").getAsInt();

		// Random booleans
		this.isPlaylist = getJO().get("isPlaylist").getAsBoolean();
		this.inheritFromFreeRoam = getJO().get("inheritFromFreeRoam").getAsBoolean();
		this.suppressOtherRewards = getJO().get("suppressOtherRewards").getAsBoolean();
		this.isPvP = getJO().get("isPvP").getAsBoolean();

		// Matchmaking
		this.isMatchmade = getMatchmaking().get("isMatchmade").getAsBoolean();
		this.minParty = getMatchmaking().get("minParty").getAsInt();
		this.maxParty = getMatchmaking().get("maxParty").getAsInt();
		this.maxPlayers = getMatchmaking().get("maxPlayers").getAsInt();
		this.requiresGuardianOath = getMatchmaking().get("requiresGuardianOath").getAsBoolean();
	}

	public JsonObject getDisplayProperties() throws APIException {
		return getJO().getAsJsonObject("displayProperties");
	}

	public JsonObject getMatchmaking() throws APIException {
		return getJO().getAsJsonObject("matchmaking");
	}

	public String getName() {
		return name;
	}

	public String getHash() {
		return hash;
	}

	public String getCompletionUnlockHash() {
		return completionUnlockHash;
	}

	public String getDestinationHash() {
		return destinationHash;
	}

	public String getPlaceHash() {
		return placeHash;
	}

	public String getActivityTypeHash() {
		return activityTypeHash;
	}

	public String getActivityModeHash() {
		return activityModeHash;
	}

	public String getDescription() {
		return description;
	}

	public String getIcon() {
		return icon;
	}

	public String getPgcrImage() {
		return pgcrImage;
	}

	public String getReleaseIcon() {
		return releaseIcon;
	}

	public int getModeType() {
		return modeType;
	}

	public int getActivityModeCategory() {
		return activityModeCategory;
	}

	public int getReleaseTime() {
		return releaseTime;
	}

	public int getActivityLightLevel() {
		return activityLightLevel;
	}

	public int getTier() {
		return tier;
	}

	public int getMinParty() {
		return minParty;
	}

	public int getMaxParty() {
		return maxParty;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public ActivityMode getActivityMode() {
		return activityMode;
	}

	public boolean isHasIcon() {
		return hasIcon;
	}

	public boolean isPlaylist() {
		return isPlaylist;
	}

	public boolean isInheritFromFreeRoam() {
		return inheritFromFreeRoam;
	}

	public boolean isSuppressOtherRewards() {
		return suppressOtherRewards;
	}

	public boolean isMatchmade() {
		return isMatchmade;
	}

	public boolean isRequiresGuardianOath() {
		return requiresGuardianOath;
	}

	public boolean isPvP() {
		return isPvP;
	}
}
