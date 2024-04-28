/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory;

import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.material.user.DestinyCharacter;

/**
 * Provides useful utility methods for Inventory Management
 *
 * All Inventory Management functions work on the presumption that you have OAuth working
 */
public class InventoryManager {

	// Account in question
	private BungieUser bungieUser;

	public InventoryManager(BungieUser bungieUser) {
		this.bungieUser = bungieUser;
	}

	public DestinyCharacter getCharacterOfType(DestinyCharacter.DestinyClass destinyClass) throws APIException {
		return bungieUser.getCharacterOfType(destinyClass);
	}

}
