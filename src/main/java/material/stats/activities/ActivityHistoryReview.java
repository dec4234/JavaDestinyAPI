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
import material.DestinyAPI;
import material.clan.Clan;
import material.manifest.ManifestEntityTypes;
import material.user.BungieUser;
import material.user.DestinyCharacter;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.*;

public class ActivityHistoryReview {

	private HttpUtils httpUtils = DestinyAPI.getHttpUtils();

	public LinkedHashMap<String, Activity> getMostUnrecentAttempts(Clan clan, ActivityIdentifier activityIdentifier) {
		HashMap<String, Activity> map = new HashMap<>();
		HashMap<String, Double> doubleMap = new HashMap<>();
		LinkedHashMap<String, Activity> toReturn = new LinkedHashMap<>();

		for(BungieUser bungieUser : clan.getMembers()) {
			for(DestinyCharacter destinyCharacter : bungieUser.getCharacters()) {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
						JsonObject jo = httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + destinyCharacter.getCharacterID() + "/Stats/Activities/?page=" + i + "&count=250&mode=" + activityIdentifier.getMode().getBungieValue());

						if (jo == null || !jo.has("Response") || !jo.getAsJsonObject("Response").has("activities")) {
							break;
						}

						JsonArray ja = jo.getAsJsonObject("Response").getAsJsonArray("activities");
						JsonObject jo2 = ja.get(0).getAsJsonObject();
						Date date = StringUtils.valueOfZTime(jo2.get("period").getAsString());

						if (map.containsKey(bungieUser.getBungieMembershipID())) {
							if (StringUtils.getDaysSinceTime(date) < StringUtils.getDaysSinceTime(map.get(bungieUser.getBungieMembershipID()).getDatePlayed())) {
								map.remove(bungieUser.getBungieMembershipID());
								map.put(bungieUser.getBungieMembershipID(), new Activity(jo2.getAsJsonObject("activityDetails").get("instanceId").getAsString(), date));
							}
						} else {
							map.put(bungieUser.getBungieMembershipID(), new Activity(jo2.getAsJsonObject("activityDetails").get("instanceId").getAsString(), date));
						}
				}
			}

			if(map.containsKey(bungieUser.getBungieMembershipID())) {
				doubleMap.put(bungieUser.getBungieMembershipID(), StringUtils.getDaysSinceTime(map.get(bungieUser.getBungieMembershipID()).getDatePlayed()));
			}
		}

		List<Map.Entry<String, Double>> list = new LinkedList<>(doubleMap.entrySet());

		Collections.sort(list, new Comparator<>() {
			public int compare(Map.Entry<String, Double> o1,
							   Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for(Map.Entry<String, Double> map2 : list) {
			toReturn.put(map2.getKey(), map.get(map2.getKey()));
		}

		return toReturn;
	}

	/**
	 * Takes a very long time
	 */
	public double getAverageCompletions(Clan clan, ActivityIdentifier activityIdentifier) {
		List<BungieUser> members = clan.getMembers();
		double count = 0;

		for (BungieUser bungieUser : members) {
			count += getCompletions(bungieUser, activityIdentifier);
		}

		return count / members.size();
	}

	public LinkedHashMap<BungieUser, Integer> getTopClearers(Clan clan, ActivityIdentifier activityIdentifier) {
		HashMap<BungieUser, Integer> map = new HashMap<>();
		LinkedHashMap<BungieUser, Integer> toReturn = new LinkedHashMap<>();

		for(BungieUser bungieUser : clan.getMembers()) {
			map.put(bungieUser, getCompletions(bungieUser, activityIdentifier));
		}

		List<Map.Entry<BungieUser, Integer>> list = new LinkedList<>(map.entrySet());

		Collections.sort(list, new Comparator<>() {
			public int compare(Map.Entry<BungieUser, Integer> o1,
							   Map.Entry<BungieUser, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for(Map.Entry<BungieUser, Integer> map2 : list) {
			toReturn.put(map2.getKey(), map2.getValue());
		}

		return toReturn;
	}

	public int getCompletions(BungieUser bungieUser, ActivityIdentifier activityIdentifier) {
		int count = 0;

		for (DestinyCharacter destinyCharacter : bungieUser.getCharacters()) {
			count += getCompletions(activityIdentifier, bungieUser, destinyCharacter);
		}

		return count;
	}

	public int getCompletions(ActivityIdentifier activityIdentifier, BungieUser bungieUser, DestinyCharacter destinyCharacter) {
		int count = 0;

		for (JsonArray ja : getArrays(activityIdentifier, bungieUser, destinyCharacter)) {
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

	public boolean hasPlayedInActivity(BungieUser bungieUser, String pgcrId) {
		Activity activity = new Activity(pgcrId);
		for (ActivityParticipant activityParticipant : activity.getParticipants()) {
			if (activityParticipant.getMembershipId().equals(bungieUser.getBungieMembershipID())) {
				return true;
			}
		}

		return false;
	}

	public void getUndiscoveredActivityHashes(BungieUser bungieUser, ActivityIdentifier activityIdentifier) {
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
						if (jo2.has("Response") && jo2.getAsJsonObject("Response").has("displayProperties") && jo2.getAsJsonObject("Response").getAsJsonObject("displayProperties").has("name")) {
							jo2 = jo2.getAsJsonObject("Response").getAsJsonObject("displayProperties");
							System.out.println(jo2.get("name").getAsString() + " - " + hash);
						}
					}

				}
			}

		}
	}

	private JsonArray[] getArrays(ActivityIdentifier activityIdentifier, BungieUser bungieUser, DestinyCharacter destinyCharacter) {
		List<JsonArray> jsonArrays = new ArrayList<>();

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				JsonObject jo = httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Account/" + bungieUser.getBungieMembershipID() + "/Character/" + destinyCharacter.getCharacterID() + "/Stats/Activities/?page=" + i + "&count=250&mode=" + activityIdentifier.getMode().getBungieValue());

				// 1665 = User has chosen for their data to be private :(
				if (jo.get("ErrorCode").getAsInt() == 1665 || !jo.getAsJsonObject("Response").has("activities")) {
					break;
				}

				JsonArray ja = jo.getAsJsonObject("Response").getAsJsonArray("activities");
				jsonArrays.add(ja);
			} catch (NullPointerException e) {
				break;
			}
		}

		return jsonArrays.toArray(new JsonArray[0]);
	}
}
