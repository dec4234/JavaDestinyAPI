/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.stats.activities;

/**
 * A collection of 2 to 4 letter identifiers for Activities
 * Also Hash Identifiers
 *
 * e.g. DSC for Deep Stone Crypt
 */
public enum ActivityIdentifier {

	// RAIDS
	LEVIATHAN("LEV", "2693136602", ActivityMode.RAID),
	EATER_OF_WORLDS("EOW", "3089205900", ActivityMode.RAID),
	SPIRE_OF_STARS("SOS", "119944200", ActivityMode.RAID),

	LAST_WISH("LW", "2122313384", ActivityMode.RAID),
	SCOURGE_OF_THE_PAST("SOTP", "548750096", ActivityMode.RAID),
	CROWN_OF_SORROW("COS", "3333172150", ActivityMode.RAID),

	GARDEN_OF_SALVATION("GOS", "8155984757", ActivityMode.RAID),

	DEEP_STONE_CRYPT("DSC", "910380154", ActivityMode.RAID),
	VAULT_OF_GLASS("VOG", "", ActivityMode.RAID);

	private String identifier;
	private String hash;
	private ActivityMode mode;

	ActivityIdentifier(String identifier, String hash, ActivityMode mode) {
		this.identifier = identifier;
		this.hash = hash;
		this.mode = mode;
	}

	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Get the directorActivityHash of the Activity
	 * Can be used to compare different activities
	 */
	public String getHash() {
		return hash;
	}

	public ActivityMode getMode() { return mode; }

	public static ActivityIdentifier fromShorthand(String shortHand) {
		for(ActivityIdentifier activityIdentifier : ActivityIdentifier.values()) {
			if(activityIdentifier.getIdentifier().equals(shortHand)) {
				return activityIdentifier;
			}
		}

		return null;
	}
}
