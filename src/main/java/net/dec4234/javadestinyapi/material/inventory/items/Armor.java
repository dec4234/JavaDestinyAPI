/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory.items;

import net.dec4234.javadestinyapi.material.user.DestinyCharacter;

public class Armor extends InventoryItem {

    public Armor(String hashID, String instanceID, DestinyCharacter characterOwner) {
        super(hashID, instanceID, characterOwner);
    }

    public Armor(String hashID, String instanceId, DestinyCharacter characterOwner, int quantity, int bindStatus, int location, String bucketHash, int transferStatus, boolean lockable, int state, int dismantlePermission, String overrideStyleItemHash, boolean isWrapper) {
        super(hashID, instanceId, characterOwner, quantity, bindStatus, location, bucketHash, transferStatus, lockable, state, dismantlePermission, overrideStyleItemHash, isWrapper);
    }
}
