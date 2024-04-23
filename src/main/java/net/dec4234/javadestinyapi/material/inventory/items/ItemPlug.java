package net.dec4234.javadestinyapi.material.inventory.items;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.manifest.DestinyManifest;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;
import org.jetbrains.annotations.NotNull;

/**
 * An ItemPlug refers to something like a mod on a gun or armor piece
 *
 * // System.out.println(httpUtils.urlRequestGET(HttpUtils.URL_BASE + "/Destiny2/3/Profile/4611686018468620320/Item/6917529335994005821/?components=305"));
 */
public class ItemPlug {

	// Outer Information
	private boolean isEnabled, isVisible;
	// Parent object info
	private JsonObject jsonObject, displayProperties, inventory, plug;
	private String hash, name, description, icon, itemTypeDisplayName, flavorText, uiItemDisplayStyle, itemTypeAndTierDisplayName, displaySource, acquireRewardSiteHash, acquireUnlockHash,
			summaryItemHash;
	private boolean hasIcon, allowActions, doesPostmasterPullHaveSideEffects, nonTransferrable, equippable, isWrapper, redacted, blacklisted;
	private String[] itemCategoryHashes;
	private ItemPerk[] itemPerks;
	// TO-DO: Action Sub-Object Info
	// Inventory Sub-Object Info
	private String bucketTypeHash, recoveryBucketTypeHash, tierTypeHash, tierTypeName, expirationTooltip, expiredInActivityMessage, expiredInOrbitMessage;
	private int maxStackSize, tierType;
	private boolean isInstanceItem, nonTransferrableOriginal, suppressExpirationWhenObjectivesComplete;
	// Plug Sub-Object Info
	private String plugCategoryIdentifier, plugCategoryHash, uiPlugLabel, alternateUiPlugLabel, actionRewardSiteHash, actionRewardItemOverrideHash, insertionMaterialRequirementHash, previewItemOverrideHash, enabledMaterialRequirementHash;
	private boolean onActionRecreateSelf, isDummyPlug, applyStatsToSocketOwnerItem;
	private int plugStyle, plugAvailibility, alternatePlugStyle;

	public ItemPlug(@NotNull String hash) throws APIException {
		this(new DestinyManifest().manifestGET(ManifestEntityTypes.INVENTORYITEM, hash));
	}

	public ItemPlug(@NotNull String hash, boolean isEnabled, boolean isVisible) throws APIException {
		this(hash);

		this.isEnabled = isEnabled;
		this.isVisible = isVisible;
	}

	public ItemPlug(@NotNull JsonObject jsonObject) {
		this.jsonObject = jsonObject;
		this.displayProperties = jsonObject.getAsJsonObject("displayProperties");
		this.inventory = jsonObject.getAsJsonObject("inventory");
		this.plug = jsonObject.getAsJsonObject("plug");


		this.hash = jsonObject.get("hash").getAsString();

		setFromObjects();
	}

	private void setFromObjects() {
		// Display Properties
		this.description = displayProperties.get("description").getAsString();
		this.name = displayProperties.get("name").getAsString();
		this.icon = displayProperties.get("icon").getAsString();
		this.hasIcon = displayProperties.get("hasIcon").getAsBoolean();

		// Inventory Properties
		this.maxStackSize = inventory.get("maxStackSize").getAsInt();
		this.bucketTypeHash = inventory.get("bucketTypeHash").getAsString();
		this.recoveryBucketTypeHash = inventory.get("recoveryBucketTypeHash").getAsString();
		this.tierTypeHash = inventory.get("tierTypeHash").getAsString();
		this.isInstanceItem = inventory.get("isInstanceItem").getAsBoolean();
		this.nonTransferrableOriginal = inventory.get("nonTransferrableOriginal").getAsBoolean();
		this.tierTypeName = inventory.get("tierTypeName").getAsString();
		this.tierType = inventory.get("tierType").getAsInt();
		this.expirationTooltip = inventory.get("expirationTooltip").getAsString();
		this.expiredInActivityMessage = inventory.get("expiredInActivityMessage").getAsString();
		this.expiredInOrbitMessage = inventory.get("expiredInOrbitMessage").getAsString();
		this.suppressExpirationWhenObjectivesComplete = inventory.get("suppressExpirationWhenObjectivesComplete").getAsBoolean();

		// Plug Properties
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	/*
	Reference taken 12/10/2021

	{
   "displayProperties":{
      "description":"High damage, high recoil.",
      "name":"Aggressive Frame",
      "icon":"/common/destiny2_content/icons/0a18b7264e9fb76764756a25d0a20fd2.png",
      "iconSequences":[
         {
            "frames":[
               "/common/destiny2_content/icons/0a18b7264e9fb76764756a25d0a20fd2.png"
            ]
         },
         {
            "frames":[
               "/common/destiny2_content/icons/a4a27f236a0724056b56d02495d8a857.png"
            ]
         }
      ],
      "hasIcon":true
   },
   "tooltipNotifications":[

   ],
   "backgroundColor":{
      "colorHash":0,
      "red":0,
      "green":0,
      "blue":0,
      "alpha":0
   },
   "itemTypeDisplayName":"Intrinsic",
   "flavorText":"",
   "uiItemDisplayStyle":"ui_display_style_intrinsic_plug",
   "itemTypeAndTierDisplayName":"Exotic Intrinsic",
   "displaySource":"",
   "action":{
      "verbName":"Dismantle",
      "verbDescription":"",
      "isPositive":false,
      "requiredCooldownSeconds":0,
      "requiredItems":[

      ],
      "progressionRewards":[

      ],
      "actionTypeLabel":"shard",
      "rewardSheetHash":0,
      "rewardItemHash":0,
      "rewardSiteHash":0,
      "requiredCooldownHash":0,
      "deleteOnAction":true,
      "consumeEntireStack":false,
      "useOnAcquire":false
   },
   "inventory":{
      "maxStackSize":1,
      "bucketTypeHash":1469714392,
      "recoveryBucketTypeHash":215593132,
      "tierTypeHash":2759499571,
      "isInstanceItem":false,
      "nonTransferrableOriginal":false,
      "tierTypeName":"Exotic",
      "tierType":6,
      "expirationTooltip":"",
      "expiredInActivityMessage":"",
      "expiredInOrbitMessage":"",
      "suppressExpirationWhenObjectivesComplete":true
   },
   "plug":{
      "insertionRules":[

      ],
      "plugCategoryIdentifier":"intrinsics",
      "plugCategoryHash":1744546145,
      "onActionRecreateSelf":false,
      "actionRewardSiteHash":0,
      "actionRewardItemOverrideHash":0,
      "insertionMaterialRequirementHash":0,
      "previewItemOverrideHash":0,
      "enabledMaterialRequirementHash":0,
      "enabledRules":[

      ],
      "uiPlugLabel":"",
      "plugStyle":0,
      "plugAvailability":0,
      "alternateUiPlugLabel":"",
      "alternatePlugStyle":0,
      "isDummyPlug":false,
      "applyStatsToSocketOwnerItem":false
   },
   "acquireRewardSiteHash":0,
   "acquireUnlockHash":0,
   "investmentStats":[

   ],
   "perks":[
      {
         "requirementDisplayString":"",
         "perkHash":886374680,
         "perkVisibility":0
      }
   ],
   "summaryItemHash":3520001075,
   "allowActions":true,
   "doesPostmasterPullHaveSideEffects":false,
   "nonTransferrable":true,
   "itemCategoryHashes":[
      59,
      2237038328,
      610365472
   ],
   "specialItemType":0,
   "itemType":19,
   "itemSubType":0,
   "classType":3,
   "breakerType":0,
   "equippable":false,
   "defaultDamageType":0,
   "isWrapper":false,
   "hash":1525239159,
   "index":5016,
   "redacted":false,
   "blacklisted":false
}
	 */
}
