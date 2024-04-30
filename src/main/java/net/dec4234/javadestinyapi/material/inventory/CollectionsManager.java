/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory;

import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.inventory.items.DestinyItem;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

public class CollectionsManager extends ContentFramework {

	private BungieUser bungieUser;

	public CollectionsManager(BungieUser bungieUser) throws APIException {
		super("https://www.bungie.net/Platform/Destiny2/" + bungieUser.getMembershipType() + "/Profile/" + bungieUser.getID() + "/?components=800", source -> {
			return source.getAsJsonObject("Response");
		});
		this.bungieUser = bungieUser;
	}

	public boolean hasCollectedItem(String collectibleHash) throws APIException {
		try {
			return getJO().getAsJsonObject("profileCollectibles").getAsJsonObject("data").getAsJsonObject("collectibles").getAsJsonObject(collectibleHash).get("state").getAsInt() == 0;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean hasCollectedItem(DestinyItem destinyItem) throws APIException {
		try {
			return getJO().getAsJsonObject("profileCollectibles").getAsJsonObject("data").getAsJsonObject("collectibles").getAsJsonObject(destinyItem.getCollectibleHash()).get("state").getAsInt() == 0;
		} catch (NullPointerException e) {
			return false;
		}
	}
}
