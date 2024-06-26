/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.user;

/**
 * Information about each platform that Destiny 2 has/is on
 *
 * Platform codes and fancy names for each are provided
 */
public enum DestinyPlatform {

	NONE(0, "None"),

	XBOX(1, "Xbox"),
	PSN(2, "PSN"),
	STEAM(3, "Steam"),
	BLIZZARD(4, "Blizzard"),
	STADIA(5, "Stadia"),
	EPIC(6, "Epic Games Store"),
	DEMON(10, "Demon"),

	// Used for searching
	BUNGIE_NEXT(254, "BungieNext"),
	ALL(-1, "All");

	private int platformCode;
	private String fancyName;

	DestinyPlatform(int platformCode, String fancyName) {
		this.platformCode = platformCode;
		this.fancyName = fancyName;
	}

	/**
	 * Get the plaform code associated with this platform
	 */
	public int getPlatformCode() {
		return platformCode;
	}

	/**
	 * Get the fancy name associated with this platofrm e.g. "Steam" instead of "STEAM"
	 * @return
	 */
	public String getFancyName() {
		return fancyName;
	}

	/**
	 * Get a DestinyPlatform from a platform code
	 */
	public static DestinyPlatform fromMembershipType(int platformCode) {
		for(DestinyPlatform destinyPlatform : DestinyPlatform.values()) {
			if(destinyPlatform.getPlatformCode() == platformCode) {
				return destinyPlatform;
			}
		}

		return null;
	}
}
