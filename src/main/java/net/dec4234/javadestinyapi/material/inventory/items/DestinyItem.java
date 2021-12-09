/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.framework.ContentInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A Destiny Inventory Item such as any weapon or armor piece
 */
public class DestinyItem implements ContentInterface {

	HttpUtils hu = DestinyAPI.getHttpUtils();

	private String hashID, name, icon, description;
	private boolean hasIcon;
	private String collectibleHash, screenshot;
	private ItemTier itemTier;

	private ItemTier tier;

	private JsonObject jo, dp;

	public DestinyItem(String hashID) {
		this.hashID = hashID;
	}

	public DestinyItem(String hashID, String name, String icon, boolean hasIcon) {
		this.hashID = hashID;
		this.name = name;
		this.icon = icon;
		this.hasIcon = hasIcon;
	}

	public String getHashID() {
		if(hashID == null) {
			checkJO();
			hashID = jo.get("hash").getAsString();
		}
		return hashID;
	}

	public int getHashIDasInt() {
		return Integer.parseInt(getHashID());
	}

	/**
	 * Gets the name of the item
	 */
	public String getName() {
		if(name == null) {
			checkDP();
			name = dp.get("name").getAsString();
		}
		return name;
	}

	/**
	 * Plug this after https://www.bungie.net/ in a browser
	 */
	public String getIcon() {
		if(icon == null) {
			checkDP();
			icon = dp.get("icon").getAsString();
		}
		return icon;
	}

	/**
	 * Gets the lore descriptions associated with this item
	 */
	public String getDescription() {
		if(description == null) {
			checkDP();
			description = dp.get("description").getAsString();
		}
		return description;
	}

	public boolean hasIcon() {
		checkDP();
		hasIcon = dp.get("hasIcon").getAsBoolean();
		return hasIcon;
	}

	public String getCollectibleHash() {
		checkJO();
		if(jo.has("collectibleHash")) {
			collectibleHash = jo.get("collectibleHash").getAsString();
		}

		return collectibleHash;
	}

	public String getScreenshot() {
		checkJO();
		if(jo.has("screenshot") && screenshot == null) {
			screenshot = jo.get("screenshot").getAsString();
		}

		return screenshot;
	}

	public ItemTier getItemTier() {
		if(itemTier == null) {
			itemTier = assessItemTier();
		}
		return itemTier;
	}

	private ItemTier assessItemTier() {
		checkJO();
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

	@Override
	public void checkJO() {
		if(jo == null) {
			jo = hu.manifestGET(ManifestEntityTypes.INVENTORYITEM, hashID).getAsJsonObject("Response");
		}
	}

	public void checkDP() {
		if(dp == null) {
			checkJO();
			dp = jo.getAsJsonObject("displayProperties");
		}
	}

	public enum ItemTier {
		COMMON,
		UNCOMMON,
		RARE,
		LEGENDARY,
		EXOTIC;
	}

	/**
	 * Return a list of all items that contain or match the name provided
	 */
	public static List<DestinyItem> searchForItems(String itemName) {
		HttpUtils httpUtils = DestinyAPI.getHttpUtils();
		List<DestinyItem> destinyItemList = new ArrayList<>();
		itemName = itemName.replace(" ", "%20");

		for(JsonElement jsonElement : httpUtils.urlRequestGET("https://www.bungie.net/Platform/Destiny2/Armory/Search/DestinyInventoryItemDefinition/" + itemName + "/").getAsJsonObject("Response").getAsJsonObject("results").getAsJsonArray("results")) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonObject displayProperties = jsonObject.getAsJsonObject("displayProperties");
			destinyItemList.add(new DestinyItem(jsonObject.get("hash").getAsString(), displayProperties.get("name").getAsString(),
												displayProperties.get("icon").getAsString(), displayProperties.get("hasIcon").getAsBoolean()));
		}
		return destinyItemList;
	}
}
