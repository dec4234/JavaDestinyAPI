package net.dec4234.javadestinyapi.tests;

import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.clan.Clan;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import org.junit.jupiter.api.Test;

public class UserTests {

	public UserTests() {

	}

	// Tests
	@Test
	public void testGetUser() {
		BungieUser bungieUser = DestinyAPI.getUserWithName("dec4234#9904");
		System.out.println(DestinyAPI.getApiKey());
		assert bungieUser != null;
		printUserInfo(bungieUser);
	}

	private void printUserInfo(BungieUser bungieUser) {
		System.out.println("Global Name: " + bungieUser.getGlobalDisplayName());
		System.out.println("Global Full Name: " + bungieUser.getSupplementalDisplayName());
		System.out.println("Global Discriminator: " + bungieUser.getDiscriminator());
		System.out.println("User Membership Type: " + bungieUser.getMembershipType());
		System.out.println("User Cross Save Override: " + bungieUser.getCrossSaveOverride());
		System.out.println("User is Public: " + bungieUser.isPublic());
		System.out.println("\n---------------------\n");
		System.out.println("Stats");
		System.out.println("Is Online: " + bungieUser.isOnline());
		System.out.println("Days since last online: " + bungieUser.getDaysSinceLastPlayed());
		System.out.println("Time Played (hours): " + (bungieUser.getTimePlayed() / 60.0));

		Clan clan;
		if((clan = bungieUser.getClan()) != null) {
			System.out.println("Clan Name: " + clan.getClanName());
		}
	}
}
