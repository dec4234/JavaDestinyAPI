/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.inventory.items;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.inventory.items.DestinyItem;
import net.dec4234.javadestinyapi.material.user.DestinyCharacter;
import net.dec4234.javadestinyapi.utils.HttpUtils;

public class InventoryItem extends DestinyItem {

	private HttpUtils httpUtils = DestinyAPI.getHttpUtils();

	private DestinyCharacter characterOwner;
	private ItemLocation itemLocation = ItemLocation.CHARACTER_INVENTORY;

	private String instanceId, bucketHash, overrideStyleItemHash;
	private InventoryBucket inventoryBucket;

	private int quantity, bindStatus, location, transferStatus, state, dismantlePermission;
	private boolean lockable = false, isWrapper;

	public InventoryItem(String hashID, String instanceId, DestinyCharacter characterOwner) {
		super(hashID);
		this.instanceId = instanceId;
		this.characterOwner = characterOwner;
	}

	/**
	 * Used if an item is in a profile-level inventory such as the "Inventory" or "Mods Page" as well as the vault
	 */
	public InventoryItem(String hashID, String instanceId, DestinyCharacter characterOwner, ItemLocation itemLocation) {
		super(hashID);
		this.instanceId = instanceId;

		this.characterOwner = characterOwner;
		this.itemLocation = itemLocation;
	}

	public InventoryItem(String hashID, String instanceId, DestinyCharacter characterOwner, int quantity, int bindStatus, int location, String bucketHash, int transferStatus, boolean lockable, int state, int dismantlePermission, boolean isWrapepr) {
		super(hashID);
		this.characterOwner = characterOwner;
		this.instanceId = instanceId;

		this.quantity = quantity;
		this.bindStatus = bindStatus;
		this.location = location;
		this.bucketHash = bucketHash;
		this.inventoryBucket = InventoryBucket.fromHash(bucketHash);
		this.transferStatus = transferStatus;
		this.lockable = lockable;
		this.state = state;
		this.dismantlePermission = dismantlePermission;
		this.isWrapper = isWrapepr;
	}

	public InventoryItem(String hashID, String instanceId, DestinyCharacter characterOwner, int quantity, int bindStatus, int location, String bucketHash, int transferStatus, boolean lockable, int state, int dismantlePermission, String overrideStyleItemHash, boolean isWrapper) {
		super(hashID);
		this.instanceId = instanceId;
		this.characterOwner = characterOwner;

		this.quantity = quantity;
		this.bindStatus = bindStatus;
		this.location = location;
		this.bucketHash = bucketHash;
		this.transferStatus = transferStatus;
		this.lockable = lockable;
		this.state = state;
		this.dismantlePermission = dismantlePermission;
		this.overrideStyleItemHash = overrideStyleItemHash;
		this.isWrapper = isWrapper;
	}

	/**
	 * Move an item from the current character to another
	 *
	 * @return Returns true if the move was succesful
	 */
	public boolean moveTo(DestinyCharacter destinyCharacter) {
		if(!isInVault()) {
			moveToVault();
		}
		JsonObject jsonObject = httpUtils.urlRequestPOSTOauth(HttpUtils.URL_BASE + "/Destiny2/Actions/Items/TransferItem/", prepareJsonObject(destinyCharacter, false, isItemEquippable()));

		boolean wasSuccesful = wasTransferSuccesful(jsonObject);

		if(wasSuccesful) {
			characterOwner = destinyCharacter;
			itemLocation = ItemLocation.CHARACTER_INVENTORY;
		}

		return wasSuccesful;
	}

	/**
	 * Move the item from its current position to the vault
	 *
	 * @return True if the move was reported as succesful
	 */
	public boolean moveToVault() {
		JsonObject jsonObject = httpUtils.urlRequestPOSTOauth(HttpUtils.URL_BASE + "/Destiny2/Actions/Items/TransferItem/", prepareJsonObject(getCharacterOwner(), true, isItemEquippable()));

		boolean wasSuccesful = wasTransferSuccesful(jsonObject);

		if(wasSuccesful) {
			characterOwner = null;
			itemLocation = ItemLocation.VAULT;
		}

		return wasSuccesful;
	}

	/**
	 * Prepares a POST body for item transferring
	 */
	private JsonObject prepareJsonObject(DestinyCharacter destinyCharacter, boolean moveToVault, boolean isEquippable) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("itemReferenceHash", getHashIDasInt());
		jsonObject.addProperty("stackSize", isEquippable ? 1 : getQuantity()); // If the item is equippable, set stack size as 1
		jsonObject.addProperty("transferToVault", moveToVault);
		jsonObject.addProperty("itemId", getInstanceIDAsLong());
		jsonObject.addProperty("characterId", destinyCharacter.getCharacterIDAsLong());
		jsonObject.addProperty("membershipType", destinyCharacter.getMembershipType());

		return jsonObject;
	}

	/**
	 * Determines whether or not an item transfer was succesful by Bungie's standards
	 */
	private boolean wasTransferSuccesful(JsonObject response) {
		try {
			return response.get("Response").getAsInt() == 0 && response.get("ErrorCode").getAsInt() == 1;
		} catch (NullPointerException ex) {
			return false;
		}
	}

	/**
	 * "Equip" this item, aka move it to the active slot
	 * There are specific restrictions on when an item can be equipped
	 * 1. If it is an exotic, the exotic slot must be free within that vertical category
	 * 2. The guardian must be offline, in orbit or at the tower
	 *
	 * @return Returns whether or not the action was succesful
	 */
	public boolean equip() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("itemId", getInstanceIDAsLong());
		jsonObject.addProperty("characterId", getCharacterOwner().getCharacterIDAsLong());
		jsonObject.addProperty("membershipType", getCharacterOwner().getMembershipType());

		JsonObject response = httpUtils.urlRequestPOSTOauth(HttpUtils.URL_BASE + "/Destiny2/Actions/Items/EquipItem/", jsonObject);

		if(response.get("ErrorCode").getAsInt() == 1641) { // Item is an exotic and cannot be equipped because there is an exotic present in another slot
			return false;
		}

		// Figure out what message is returned when item cannot be equipped at that characters present location
		if(response.get("ErrorCode").getAsInt() == 1641) { // Character is in a location that does not permit item equipping through the API
			return false;
		}

		return true;
	}

	/**
	 * Needs more testing
	 *
	 * Is the item equippable in the invenory bucket that it belongs to
	 * Sometimes not possible like if it is in the vault or inventory tab
	 */
	public boolean isItemEquippable() {
		InventoryBucket inventoryBucket = getInventoryBucket();

		return inventoryBucket.isEquippable();
	}

	/**
	 * Lock or unlock an item
	 * @param state The state of the lock the item should be set to
	 */
	public void setLockState(boolean state) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("state", state);
		jsonObject.addProperty("itemId", getInstanceId());
		jsonObject.addProperty("characterId", getCharacterOwner().getCharacterIDAsLong());
		jsonObject.addProperty("membershipType", getCharacterOwner().getMembershipType());


		httpUtils.urlRequestPOSTOauth(HttpUtils.URL_BASE + "/Destiny2/Actions/Items/SetLockState/", jsonObject.toString());
	}

	/**
	 * Is the item capable of being locked
	 */
	public boolean isItemLockable() {
		return this.lockable;
	}

	public enum ItemLocation {
		CHARACTER_INVENTORY,
		PROFILE_INVENTORY,
		MOD_INVENTORY,
		VAULT;
	}

	/**
	 * The character the item is currently in the inventory of
	 * will be null if the item is in the vault
	 */
	public DestinyCharacter getCharacterOwner() { return characterOwner; }

	public ItemLocation getItemLocation() {
		return itemLocation;
	}

	public boolean isInVault() {
		return getItemLocation() == ItemLocation.VAULT;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public long getInstanceIDAsLong() { return Long.parseLong(getInstanceId()); }

	public String getBucketHash() {
		return bucketHash;
	}

	public InventoryBucket getInventoryBucket() { return InventoryBucket.fromHash(getBucketHash()); }

	public String getOverrideStyleItemHash() {
		return overrideStyleItemHash;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getBindStatus() {
		return bindStatus;
	}

	public int getLocation() {
		return location;
	}

	public int getTransferStatus() {
		return transferStatus;
	}

	public int getState() {
		return state;
	}

	public int getDismantlePermission() {
		return dismantlePermission;
	}

	public boolean isLockable() {
		return lockable;
	}

	public boolean isWrapper() {
		return isWrapper;
	}
}
