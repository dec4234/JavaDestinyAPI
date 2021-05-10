/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.stats.activities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import material.manifest.ManifestEntityTypes;
import material.user.BungieUser;
import material.user.DestinyCharacter;
import utils.HttpUtils;

public class ActivityHistoryReview {

	private HttpUtils httpUtils = new HttpUtils();

	private BungieUser bungieUser;

	public ActivityHistoryReview(BungieUser bungieUser) {
		this.bungieUser = bungieUser;
	}

	public int getCompletions(ActivityIdentifier activityIdentifier) {
		int count = 0;

		for (DestinyCharacter destinyCharacter : bungieUser.getCharacters()) {
			System.out.println(getCompletions(activityIdentifier, destinyCharacter));
			count += getCompletions(activityIdentifier, destinyCharacter);
		}

		return count;
	}

	public int getCompletions(ActivityIdentifier activityIdentifier, DestinyCharacter destinyCharacter) {
		int count = 0;

		for (int i = 0; i < 25; i++) {
			JsonObject jo = httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + destinyCharacter.getCharacterID() + "/Stats/Activities/?page=" + i + "&count=250&mode=" + activityIdentifier.getMode().getBungieValue());

			if (!jo.getAsJsonObject("Response").has("activities")) {
				break;
			}

			JsonArray ja = jo.getAsJsonObject("Response").getAsJsonArray("activities");
			for (JsonElement je : ja) {
				JsonObject jo1 = je.getAsJsonObject();
				for (String s : activityIdentifier.getHashes()) {
					if (jo1.getAsJsonObject("activityDetails").get("referenceId").getAsString().equals(s)) {
						if (jo1.getAsJsonObject("values").getAsJsonObject("completed").getAsJsonObject("basic").get("value").getAsDouble() == 1) {
							count++;
						}
					}
				}
			}
		}

		return count;
	}

	public void getUndiscoveredActivityHashes(ActivityIdentifier activityIdentifier) {
		for (DestinyCharacter destinyCharacter : bungieUser.getCharacters()) {
			for (int i = 0; i < 25; i++) {
				JsonObject jo = httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + destinyCharacter.getCharacterID() + "/Stats/Activities/?page=" + i + "&count=250&mode=" + activityIdentifier.getMode().getBungieValue());

				if (!jo.getAsJsonObject("Response").has("activities")) {
					break;
				}

				JsonArray ja = jo.getAsJsonObject("Response").getAsJsonArray("activities");
				for (JsonElement je : ja) {
					JsonObject jo1 = je.getAsJsonObject();
					String hash = jo1.getAsJsonObject("activityDetails").get("referenceId").getAsString();
					if (ActivityIdentifier.fromHash(hash) == null) {
						JsonObject jo2 = httpUtils.manifestGET(ManifestEntityTypes.ACTIVITY, hash);
						if(jo2.has("Response") && jo2.getAsJsonObject("Response").has("displayProperties") && jo2.getAsJsonObject("Response").getAsJsonObject("displayProperties").has("name")) {
							jo2 = jo2.getAsJsonObject("Response").getAsJsonObject("displayProperties");
							System.out.println(jo2.get("name").getAsString() + " - " + hash);
						}
					}

				}
			}

		}
	}
}
