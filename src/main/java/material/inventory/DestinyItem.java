/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.inventory;

import com.google.gson.JsonObject;
import material.manifest.ManifestEntityTypes;
import utils.HttpUtils;

/**
 * A Destiny Inventory Item such as any weapon or armor piece
 */
public class DestinyItem {

	HttpUtils hu = new HttpUtils();

	private String hashID;
	private String name;
	private String icon;
	private String description;
	private boolean hasIcon;
	private String collectibleHash;
	private String screenshot;
	private ItemTier itemType;

	private ItemTier tier;

	private JsonObject jo;

	public DestinyItem(String hashID) {
		this.hashID = hashID;
		jo = hu.manifestGET(ManifestEntityTypes.INVENTORYITEM, hashID).getAsJsonObject("Response");
		assignValues();
	}

	private void assignValues() {
		JsonObject dp = jo.getAsJsonObject("displayProperties");
		name = dp.get("name").getAsString();
		description = dp.get("description").getAsString();
		icon = dp.get("icon").getAsString();
		hasIcon = dp.get("hasIcon").getAsBoolean();
		collectibleHash = jo.get("collectibleHash").getAsString();
		screenshot = jo.get("screenshot").getAsString();
		itemType = assessItemTier();
	}

	public String getHashID() {
		return hashID;
	}

	/**
	 * Gets the name of the item
	 */
	public String getName() {
		return name;
	}

	/**
	 * Plug this after https://www.bungie.net/ in a browser
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Gets the lore descriptions associated with this item
	 */
	public String getDescription() {
		return description;
	}

	public boolean hasIcon() {
		return hasIcon;
	}

	public String getCollectibleHash() {
		return collectibleHash;
	}

	public ItemTier assessItemTier() {
		switch(jo.getAsJsonObject("inventory").get("tierTypeName").getAsString()) {
			case "Common":
				return ItemTier.COMMON;
			case "Uncommon":
				return ItemTier.UNCOMMON;
			case "Rare":
				return ItemTier.RARE;
			case "Legendary":
				return ItemTier.LEGENDARY;
			case "Exotic":
				return ItemTier.EXOTIC;
		}
		return null;
	}

	enum ItemTier {
		COMMON,
		UNCOMMON,
		RARE,
		LEGENDARY,
		EXOTIC;
	}
}
