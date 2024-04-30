/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.user;

/**
 * A "UserCredential" is a linked Platform to a Bungie Account
 *
 * Such as a twitch account
 */
public class UserCredential {

	private UserCredentialType userCredentialType;
	private boolean isPublic;
	private String credentialDisplayName;
	private String credentialAsString;

	public UserCredential(UserCredentialType userCredentialType, boolean isPublic) {
		this.userCredentialType = userCredentialType;
		this.isPublic = isPublic;
	}

	public UserCredential(UserCredentialType userCredentialType, boolean isPublic, String credentialDisplayName) {
		this.userCredentialType = userCredentialType;
		this.isPublic = isPublic;
		this.credentialDisplayName = credentialDisplayName;
	}

	public UserCredential(UserCredentialType userCredentialType, boolean isPublic, String credentialDisplayName, String credentialAsString) {
		this.userCredentialType = userCredentialType;
		this.isPublic = isPublic;
		this.credentialDisplayName = credentialDisplayName;
		this.credentialAsString = credentialAsString;
	}

	public UserCredentialType getUserCredentialType() {
		return userCredentialType;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public String getCredentialDisplayName() {
		return credentialDisplayName;
	}

	public String getCredentialAsString() {
		return credentialAsString;
	}
}
