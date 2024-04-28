/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory.items;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.manifest.DestinyManifest;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;

public class ItemPerk {

	private JsonObject jsonObject;
	private String requirementDisplayString, perkHash, iconPath;
	private int perkVisibility;
	private boolean isActive, visible;

	public ItemPerk(String perkHash) throws APIException {
		this(new DestinyManifest().manifestGET(ManifestEntityTypes.INVENTORYITEM, perkHash));
	}

	public ItemPerk(JsonObject jsonObject) {
		System.out.println(jsonObject);
		this.jsonObject = jsonObject;
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}
}
