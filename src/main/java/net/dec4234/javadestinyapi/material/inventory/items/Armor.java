package net.dec4234.javadestinyapi.material.inventory.items;

import net.dec4234.javadestinyapi.material.user.DestinyCharacter;

public class Armor extends InventoryItem {

    public Armor(DestinyItem destinyItem, String instanceId, DestinyCharacter characterOwner, int quantity, int bindStatus, int location, String bucketHash, int transferStatus, boolean lockable, int state, int dismantlePermission, String overrideStyleItemHash, boolean isWrapper) {
        super(destinyItem, instanceId, characterOwner, quantity, bindStatus, location, bucketHash, transferStatus, lockable, state, dismantlePermission, overrideStyleItemHash, isWrapper);
    }
}
