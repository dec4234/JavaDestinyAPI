/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.inventory;

import material.user.BungieUser;
import utils.framework.ContentFramework;

public class CollectionsManager extends ContentFramework {

	private BungieUser bungieUser;

	public CollectionsManager(BungieUser bungieUser) {
		super("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Profile/" + bungieUser.getBungieMembershipID() + "/?components=800", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieUser = bungieUser;
	}

	public boolean hasCollectedItem(String collectibleHash) {
		try {
			return getJO().getAsJsonObject("profileCollectibles").getAsJsonObject("data").getAsJsonObject("collectibles").getAsJsonObject(collectibleHash).get("state").getAsInt() == 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean hasCollectedItem(DestinyItem destinyItem) {
		try {
			return getJO().getAsJsonObject("profileCollectibles").getAsJsonObject("data").getAsJsonObject("collectibles").getAsJsonObject(destinyItem.getCollectibleHash()).get("state").getAsInt() == 0;
		} catch (NullPointerException e) {
			return false;
		}
	}
}
