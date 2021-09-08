/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.user;

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
