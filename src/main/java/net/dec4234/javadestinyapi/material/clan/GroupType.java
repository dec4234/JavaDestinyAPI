/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.clan;

/**
 * The group type. This doesn't really matter since Clans are the only type of group. But, this is included for future
 * compatibility and clarity.
 */
public enum GroupType {

	GENERAL(0),
	CLAN(1);

	private int type;

	GroupType(int type) {
		this.type = type;
	}

	/**
	 * @return the integer representation of the type
	 */
	public int getType() {
		return type;
	}
}
