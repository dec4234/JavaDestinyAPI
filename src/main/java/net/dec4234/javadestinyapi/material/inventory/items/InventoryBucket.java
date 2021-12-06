/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory.items;

/**
 * An inventory Bucket is a specific slot which contains a group of items,
 * such as heavy weapons or helmets
 */
public enum InventoryBucket {
	KINETIC("1498876634", true),
	SPECIAL("2465295065", true),
	HEAVY("953998645", true),
	GHOST("4023194814", true),
	ARTIFACT(""),

	HELMET("3448274439", true),
	ARMS("3551918588", true),
	CHESTPLATE("14239492", true),
	BOOTS("20886954", true),
	CLASS_ITEM("1585787867", true),

	SPARROW("", true),
	SHIP("284967655", true),
	EMBLEM("4274335291", true),
	FINISHER("3683254069"),
	EMOTE(""),
	UNSEEN_EMOTE("2401704334"),

	ENGRAM("375726501"),
	SEASONAL_TOOLS("1345459588"),

	PROFILE_INVENTORY("1469714392"),
	MOD_INVENTORY("3313201758"),
	VAULT("138197802"),
	QUEST_MANAGER("1558457900");

	private String hash;
	private boolean isEquippable = false;

	InventoryBucket(String hash) {
		this.hash = hash;
	}

	InventoryBucket(String hash, boolean isEquippable) {
		this.hash = hash;
		this.isEquippable = isEquippable;
	}

	public String getHash() {
		return hash;
	}

	public boolean isEquippable() { return isEquippable; }

	public static InventoryBucket fromHash(String hash) {
		for (InventoryBucket inventoryBucket : InventoryBucket.values()) {
			if (inventoryBucket.getHash().equals(hash)) {
				return inventoryBucket;
			}
		}

		return null;
	}
}
