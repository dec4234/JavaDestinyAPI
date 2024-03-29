/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.manifest;

/**
 * Stores definitions as seen on https://www.bungie-net.github.io/
 * Used for accessing things in the Destiny Manifest
 * Sorted by category, then by alphabetical order
 */
public enum ManifestEntityTypes {

	// Relating to items/character's inventory
	ARTIFACT("DestinyArtifactDefinition"),
	BREAKERTYPE("DestinyBreakerTypeDefinition"),
	COLLECTIBLE("DestinyCollectibleDefinition"),
	EQUIPMENTSLOT("DestinyEquipmentSlotDefinition"),
	INVENTORYBUCKET("DesintInventoryBucketDefinition"),
	/** Any item that can go in a user's invenory such as armor, weapons, and planetary materials */
	INVENTORYITEM("DestinyInventoryItemDefinition"),
	ITEMCATEGORY("DestinyItemCategoryDefinition"),
	ITEMSTAT("DestinyStatDefinition"),
	ITEMSTATGROUP("DestinyStatGroupDefinition"),
	ITEMTIER("DestinyItemTierTypeDefinition"),
	MATERIALREQUIREMENTSET("DestinyMaterialRequirementSetDefinition"),
	POWERCAP("DestinyPowerCapDefinition"),
	RECORD("DestinyRecordDefinition"),
	REWARDSOURCE("DestinyRewardSourceDefinition"),
	SANDBOXPERK("DestinySandboxPerkDefinition"),
	TALENTGRID("DestinyTalentGridDefinition"),

	// Relating to users
	CLASS("DestinyClassDefinition"),
	GENDER("DestinyGenderDefinition"),
	MILESTONE("DestinyMilestoneDefinition"),
	PROGRESSION("DestinyProgressionDefinition"),
	RACE("DestinyRaceDefinition"),

	// Vendors/world
	ACTIVITY("DestinyActivityDefinition"),
	ACTIVITYGRAPH("DestinyActivityGraphDefinition"),
	ACTIVITYMODE("DestinyActivityModeDefinition"),
	ACTIVITYMODIFIER("DestinyActivityModifierDefinition"),
	ACTIVITYTYPE("DestinyActivityTypeDefinition"),
	DAMAGETYPE("DestinyDamageTypeDefinition"),
	DESTINATION("DestinyDestinationDefinition"),
	FACTION("DestinyFactionDefinition"),
	LOCATION("DestinyLocationDefinition"),
	OBJECTIVE("DestinyObjectiveDefinition"),
	PLACE("DestinyPlaceDefinition"),
	VENDOR("DestinyVendorDefinition"),
	VENDORGROUP("DestinyVendorGroupDefinition"),

	// Misc
	CHECKLIST("DestinyChecklistDefinition"),
	ENERGYTYPE("DestinyEnergyTypeDefinition"),
	HISTORICALSTATS("DestinyHistoricalStatsDefinition"),
	PRESENTATIONNODE("DestinyPresentationNodeDefinition"),
	LORE("DestinyLoreDefinition"),
	METRIC("DestinyMetricDefinition"),
	PLUGSET("DestinyPlugSetDefinition"),
	REPORTREASONCATEGORY("DestinyReportReasonCategoryDefinition"),
	SEASON("DestinySeasonDefinition"),
	SEASONPASS("DestinySeasonPassDefinition"),
	SOCKETCATEGORY("DestinySocketCategoryDefinition"),
	SOCKETTYPE("DestinySocketTypeDefinition"),
	TAGMETADATA("TagMetadataDefinition"),
	TRAIT("DestinyTraitDefinition"),
	TRAITCATEGORY("DestinyTraitCategoryDefinition"),
	UNLOCK("DestinyUnlockDefinition");

	String bungieEntityValue;

	private ManifestEntityTypes(String bungieEntityValue) {
		this.bungieEntityValue = bungieEntityValue;
	}

	public String getBungieEntityValue() {
		return bungieEntityValue;
	}
}
