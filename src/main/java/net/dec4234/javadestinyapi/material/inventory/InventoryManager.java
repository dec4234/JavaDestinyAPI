/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory;

import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.material.user.DestinyCharacter;

/**
 * Provides useful utility methods for Inventory Management
 *
 * All Inventory Management functions work on the presumption that you have OAuth working
 *
 */
public class InventoryManager {

	// Account in question
	private BungieUser bungieUser;

	public InventoryManager(BungieUser bungieUser) {
		this.bungieUser = bungieUser;
	}

	public DestinyCharacter getCharacterOfType(DestinyCharacter.DestinyClass destinyClass) {
		return bungieUser.getCharacterOfType(destinyClass);
	}

}
