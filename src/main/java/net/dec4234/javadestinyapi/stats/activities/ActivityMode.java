/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.stats.activities;

/**
 * A list of activity mode numbers according to bungie.net
 * Some numbers are randomly skipped or "reserved", thus they are not listed here
 * https://bungie-net.github.io/multi/schema_Destiny-Definitions-DestinyActivityDefinition.html
 */
public enum ActivityMode {

	NONE(0),
	STORY(2),
	STRIKE(3),
	RAID(4),
	ALL_PVP(5),
	PATROL(6),
	ALL_PVE(7),
	CONTROL(10),
	CLASH(12),
	CRIMSON_DOUBLES(15),
	NIGHTFALL(16),
	HEROIC_NIGHTFALL(17),
	ALL_STRIKES(18),
	IRON_BANNER(19),
	ALL_MAYHEM(25),
	SUPREMACY(31),
	PRIVATE_MATCHES_ALL(32),
	SURVIVAL(37),
	COUNTDOWN(38),
	TRIALS_OF_THE_NINE(39),
	SOCIAL(40),
	TRIALS_COUNTDOWN(41),
	TRIALS_SURVIVAL(42),
	IBCONTROL(43),
	IBCLASH(44),
	IBSUPREMACY(45),
	SCOREDNF(46),
	SCOREDHEROICNF(47),
	RUMBLE(48),
	ALL_DOUBLES(49),
	DOUBLES(50),
	PM_CLASH(51),
	PM_CONTROL(52),
	PM_SUPREMACY(53),
	PM_COUNTDOWN(54),
	PM_SURVIVAL(55),
	PM_MAYHEM(56),
	PM_RUMBLE(57),
	HEROIC_ADVENTURE(58),
	SHOWDOWN(59),
	LOCKDOWN(60),
	SCORCHED(61),
	SCORCHED_TEAM(62),
	GAMBIT(63),
	ALL_PVE_COMP(64),
	BREAKTHROUGH(65),
	BLACK_ARMORY_RUN(66),
	SALVAGE(67),
	IRON_BANNER_SALVAGE(68),
	PVP_COMP(69),
	PVP_QUICKPLAY(70),
	CLASH_QUICKPLAY(71),
	CLASH_COMP(72),
	CONTROL_QUICKPLAY(73),
	CONTROL_COMP(74),
	GAMBIT_PRIME(75),
	RECKONING(76),
	MENAGERIE(77),
	VEX_OFFENSIVE(78),
	NIGHTMARE_HUNT(79),
	ELIMINATION(80),
	MOMENTUM(81),
	DUNGEON(82),
	SUNDIAL(83),
	TRIALS_OF_OSIRIS(84);


	private int bungieValue;

	private ActivityMode(int bungieValue) {
		this.bungieValue = bungieValue;
	}

	public int getBungieValue() {
		return bungieValue;
	}

	public static ActivityMode fromBungieValue(int bungieValue) {
		for(ActivityMode activityMode : ActivityMode.values()) {
			if(activityMode.getBungieValue() == bungieValue) {
				return activityMode;
			}
		}

		return null;
	}
}
