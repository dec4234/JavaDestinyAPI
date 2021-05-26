/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.user;

/**
 * Not to be confused with DestinyPlatform
 *
 * Used to identify linked platforms on a bungie.net account such as
 * links to twitch and steam
 */
public enum UserCredentialType {

	// When an account has no associated links
	NONE(0, "None"),
	WILD(3, "Wild"),
	FAKE(4, "Fake"),

	// Original consoles
	XBOX_ID(1, "Xbox ID"),
	PSN_ID(2, "PSN ID"),

	// PC platforms
	STEAM_ID(12, "Steam ID"),
	BATTLENET_ID(14, "Battle.net ID"),

	// Later Platforms
	STADIA_ID(16, "Stadia ID"),

	// Social Media and misc.
	FACEBOOK(5, "Facebook"),
	GOOGLE(8, "Google"),
	WINDOWS(9, "Windows"),
	DEMON_ID(10, "Demon ID"),
	TWITCH_ID(19, "Twitch ID");

	private int platformCode;
	private String fancyName;

	UserCredentialType(int platformCode, String fancyName) {
		this.platformCode = platformCode;
		this.fancyName = fancyName;
	}

	public int getPlatformCode() {
		return platformCode;
	}

	public String getFancyName() {
		return fancyName;
	}

	public static UserCredentialType fromPlatformCode(int platformCode) {
		for(UserCredentialType userCredentialType : UserCredentialType.values()) {
			if(userCredentialType.getPlatformCode() == platformCode) {
				return userCredentialType;
			}
		}

		return null;
	}
}
