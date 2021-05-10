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
import material.user.BungieUser;
import material.user.DestinyCharacter;
import utils.HttpUtils;

public class ActivityHistoryReview {

	private HttpUtils httpUtils = new HttpUtils();

	private BungieUser bungieUser;
	private DestinyCharacter destinyCharacter;

	public ActivityHistoryReview(BungieUser bungieUser) {
		this.bungieUser = bungieUser;
	}

	public ActivityHistoryReview(BungieUser bungieUser, DestinyCharacter destinyCharacter) {
		this.bungieUser = bungieUser;
		this.destinyCharacter = destinyCharacter;
	}

	public int getCompltetions(ActivityIdentifier activityIdentifier) {
		int count = 0;

		for(DestinyCharacter destinyCharacter : bungieUser.getCharacters()) {
			count += getCompletions(activityIdentifier, destinyCharacter);
		}

		return count;
	}

	public int getCompletions(ActivityIdentifier activityIdentifier, DestinyCharacter destinyCharacter) {
		int count = 0;

		for(int i = 0; i < 25; i++) {
			JsonObject jo = httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + destinyCharacter.getCharacterID() + "/Stats/Activities/?page=" + i + "&count=250&mode=" + activityIdentifier.getMode().getBungieValue());

			if(!jo.getAsJsonObject("Response").has("activities")) {
				break;
			}

			JsonArray ja = jo.getAsJsonObject("Response").getAsJsonArray("activities");
			for(JsonElement je : ja) {
				JsonObject jo1 = je.getAsJsonObject();
				if(jo1.getAsJsonObject("activityDetails").get("directorActivityHash").getAsString().equals(activityIdentifier.getHash())) {
					if (jo1.getAsJsonObject("values").getAsJsonObject("completed").getAsJsonObject("basic").get("value").getAsInt() == 1) {
						count++;
					}
				}
			}
		}

		return count;
	}
}
