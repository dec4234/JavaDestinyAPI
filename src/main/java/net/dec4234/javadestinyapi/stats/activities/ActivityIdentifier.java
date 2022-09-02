/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.stats.activities;

/**
 * A collection of 2 to 4 letter identifiers for Activities
 * Also Hash Identifiers
 *
 * e.g. DSC for Deep Stone Crypt
 */
public enum ActivityIdentifier {

	// STRIKES
	ARMS_DEALER("AD", ActivityMode.STRIKE, "442671778", "2080275457", "2378719026", "2724706103", "2378719025", "770196931", "3240321863", "1258914202", "1679518121"),
	LAKE_OF_SHADOWS("LOS", ActivityMode.STRIKE, "2318521576", "3711627564", "3725993747", "2630091891", "4134816102"),

	THE_DISGRACED("TD", ActivityMode.STRIKE, "1684420962", "174131855"),
	FALLEN_SABER("FS", ActivityMode.STRIKE, "3597990372", "3777220691"),
	DEVILS_LAIR("DL", ActivityMode.STRIKE, "969982762"),

	SAVATHUNS_SONG("SS", ActivityMode.STRIKE, "2359594803", "1101792305", "3191123858", "649648599", "1542611209"),

	INVERTED_SPIRE("IS", ActivityMode.STRIKE, "3704910925", "1563393783", "286562305", "1107473294", "1743518003", "338662534", "2753180142", "1743518000", "467266668"),
	EXODUS_CRASH("EC", ActivityMode.STRIKE, "2459768558", "1549614516", "4260306233", "1930116823", "2479262829", "1930116820", "2971335647"),
	INSIGHT_TERMINUS("IT", ActivityMode.STRIKE, "3751421841", "291911094", "3735153516", "3735153519"),
	PROVING_GROUND("PG", ActivityMode.STRIKE, "546528643", "1754609040"),

	THE_PYRAMIDION("P", ActivityMode.STRIKE, "1035135049", "1603374112", "1332567112", "2704613535", "1332567115", "981383202", "2799837309", "4261351281"),
	FESTERING_CORE("FC", ActivityMode.STRIKE, "1035850837", "3596828104"),

	TREE_OF_PROBABILITIES("TOP", ActivityMode.STRIKE, "2678510381", "1263901594", "561345572", "561345575", "840678113", "4085493024", "2684121894"),
	A_GARDEN_OF_WORLD("AGOW", ActivityMode.STRIKE, "656703508", "3676029623", "2230236215", "2230236212", "689927878", "117447065", "2579344189", "743963294"),

	STRANGE_TERRAIN("ST", ActivityMode.STRIKE, "2992505404", "861639649", "3801775390", "2248296964", "861639650"),
	WILL_OF_THE_THOUSANDS("WOTT", ActivityMode.STRIKE, "1198216109", "3944547192", "3510043585", "1317492847", "1891220709", "3944547195"),

	WARDEN_OF_NOTHING("WON", ActivityMode.STRIKE, "1360385764", "1360385767", "1134446996", "1493405720"),
	THE_HOLLOWED_LAIR("THL", ActivityMode.STRIKE, "663301842", "1475539136", "1475539139", "955874134"),
	BROODHOLD("BRO", ActivityMode.STRIKE, "1666283939", "3813623455"),

	THE_CORRUPTED("TC", ActivityMode.STRIKE, "3374205762", "723056533", "224295651"),

	THE_SCARLET_KEEP("TSK", ActivityMode.STRIKE, "1775791936", "3879143309", "3643233460", "2047723007", "346345236"),

	THE_GLASSWAY("TG", ActivityMode.STRIKE, "2226120409", "3965479856", "3329390423"),

	QUEST_EXODUS_CRASH("QEC", ActivityMode.STRIKE, "940394831"),

	// NIGHTFALLS
	ORDEAL_ADEPT("OA", ActivityMode.ALL_STRIKES, "887176540", "3265488360", "1203950596", "1753547897"),
	ORDEAL_HERO("OH", ActivityMode.ALL_STRIKES, "3849697859", "887176543", "3265488363", "2136458567", "265186830", "1203950599", "3293630131", "1358381371"),
	ORDEAL_LEGEND("OL", ActivityMode.ALL_STRIKES, "3849697858", "1302909045", "2136458566", "265186831", "2599001913", "1495545954", "3233498448"),
	ORDEAL_MASTER("OM", ActivityMode.ALL_STRIKES, "3233498455", "2136458561", "2136458567"),
	ORDEAL_GRANDMASTER("OGM", ActivityMode.ALL_STRIKES, "2136458560", "1753547901", "265186825"),

	ARMS_DEALER_NF("ADNF", ActivityMode.NIGHTFALL, "145302664", "3145298904"),
	ARMS_DEALER_NF_PRESTIGE("ADNFP", ActivityMode.ALL_STRIKES, "601540706"),
	QUEST_ARMS_DEALER_PRESTIGE("QADP", ActivityMode.HEROIC_NIGHTFALL, "1207505828"),
	LAKE_OF_SHADOWS_NF("LOSNF", ActivityMode.ALL_STRIKES, "3372160277"),

	SAVATHUNS_SONG_NF("SSNF", ActivityMode.NIGHTFALL, "1975064760", "3280234344"),
	SAVATHUNS_SONG_NF_PRESTIGE("SSNFP", ActivityMode.ALL_STRIKES, "585071442"),

	INVERTED_SPIRE_NF("ISNF", ActivityMode.NIGHTFALL, "3368226533", "4259769141"),
	INVERTED_SPIRE_NF_PRESTIGE("ISNFP", ActivityMode.NIGHTFALL, "3050465729"),
	EXODUS_CRASH_NF("ECNF", ActivityMode.ALL_STRIKES, "1282886582"),
	INSIGHT_TERMINUS_NF("ITNF", ActivityMode.ALL_STRIKES, "1034003646"),

	THE_PYRAMIDION_NF("TPNF", ActivityMode.NIGHTFALL, "926940962", "3289589202"),
	THE_PYRAMIDION_NF_PRESTIGE("TPNFP", ActivityMode.HEROIC_NIGHTFALL, "1129066976"),
	UNIDENTIFIED_FRAME_THE_PYRAMIDION("UFTP", ActivityMode.HEROIC_NIGHTFALL, "1431348899"),

	TREE_OF_PROBABILITIES_NF("TOPNF", ActivityMode.NIGHTFALL, "2046332536"),
	TREE_OF_PROBABILITIES_NF_PRESTIGE("TOPNFP", ActivityMode.NIGHTFALL, "2416546450"),
	A_GARDEN_WORLD_NF("AGWNF", ActivityMode.ALL_STRIKES, "2322829199", "936308438"),
	A_GARDEN_WORLD_NF_PRESTIGE("AGWP", ActivityMode.ALL_STRIKES, "2688061647"),

	STRANGE_TERRAIN_NF("STNF", ActivityMode.ALL_STRIKES, "2179568029", "4279557030", "522318687"),
	STRANGE_TERRAIN_NF_PRESTIGE("STNFP", ActivityMode.ALL_STRIKES, "1794007817"),
	WILL_OF_THE_THOUSANDS_NF("WOTT", ActivityMode.NIGHTFALL, "3907468134", "272852450"),
	WILL_OF_THE_THOUSANDS_NF_PRESTIGE("WOTTP", ActivityMode.NIGHTFALL, "2383858990"),

	THE_HOLLOWED_LAIR_NF("THLNF", ActivityMode.ALL_STRIKES, "3701132453"),
	WARDEN_OF_NOTHING_NF("WONNF", ActivityMode.ALL_STRIKES, "3108813009"),

	THE_CORRUPTED_NF("TCNF", ActivityMode.NIGHTFALL, "3388474648", "3034843176"),

	// LOST SECTORS
	BUNKER_E15_LEGEND("BE15L", ActivityMode.ALL_STRIKES, "1648125541"),
	BUNKER_E15_MASTER("BE15M", ActivityMode.ALL_STRIKES, "1648125538"),
	K1_CREW_QUARTERS_LEGEND("K1CQL", ActivityMode.ALL_STRIKES, "184186581"),
	CONCEALED_VOID_LEGEND("CVL", ActivityMode.ALL_STRIKES, "912873277"),

	// RAIDS
	LEVIATHAN("LEV", "https://www.bungie.net/img/destiny_content/pgcr/raid_gluttony.jpg", ActivityMode.RAID, "2693136600", "2693136601", "2693136602", "2693136603", "2693136604", "2693136605"),
	LEVIATHAN_PRESTIGE("LEVP", "https://www.bungie.net/img/destiny_content/pgcr/raid_gluttony.jpg", ActivityMode.RAID, "1685065161", "3446541099", "3879860661", "417231112", "2449714930", "757116822"),
	EATER_OF_WORLDS("EOW", "https://www.bungie.net/img/destiny_content/pgcr/raids_leviathan_eater_of_worlds.jpg", ActivityMode.RAID, "3089205900"),
	EATER_OF_WORLDS_PRESTIGE("EOWP", "https://www.bungie.net/img/destiny_content/pgcr/raids_leviathan_eater_of_worlds.jpg", ActivityMode.RAID, "809170886"),
	SPIRE_OF_STARS("SOS", "https://www.bungie.net/img/destiny_content/pgcr/raid_greed.jpg", ActivityMode.RAID, "119944200"),
	SPIRE_OF_STARS_PRESTIGE("SOSP", "https://www.bungie.net/img/destiny_content/pgcr/raid_greed.jpg", ActivityMode.RAID, "3213556450"),

	LAST_WISH("LW", "https://www.bungie.net/img/destiny_content/pgcr/raid_beanstalk.jpg", ActivityMode.RAID, "2122313384"),
	SCOURGE_OF_THE_PAST("SOTP", "https://www.bungie.net/img/destiny_content/pgcr/raids.1305rh0093145r13t5hn10tnz.raid_sunset.jpg", ActivityMode.RAID, "548750096"),
	CROWN_OF_SORROW("COS", "https://www.bungie.net/img/destiny_content/pgcr/raid_eclipse.jpg", ActivityMode.RAID, "3333172150"),

	GARDEN_OF_SALVATION("GOS", "https://www.bungie.net/img/destiny_content/pgcr/raid_garden_of_salvation.jpg", ActivityMode.RAID, "2659723068", "3458480158"),

	DEEP_STONE_CRYPT("DSC", "https://www.bungie.net/img/destiny_content/pgcr/europa-raid-deep-stone-crypt.jpg", ActivityMode.RAID, "910380154"),
	VAULT_OF_GLASS("VOG", "https://www.bungie.net/pubassets/pkgs/150/150569/FrontpageBanner_1920x590.jpg?cv=3983621215&av=1926358162", ActivityMode.RAID, "3881495763"),
	VAULT_OF_GLASS_MASTER("VOGM", "https://www.bungie.net/pubassets/pkgs/150/150569/FrontpageBanner_1920x590.jpg?cv=3983621215&av=1926358162", ActivityMode.RAID, ""),

	VOW_OF_THE_DISCIPLE("VOTD", "https://www.bungie.net/pubassets/pkgs/157/157111/FrontPageBanner_1920x590.jpg", ActivityMode.RAID, ""),

	KF("KF", "https://www.bungie.net/pubassets/pkgs/170/170501/ArticleBanner_997x500.jpg", ActivityMode.RAID, ""); // TODO: Hashes for KF and VOTD

	private String identifier;
	private String[] hashes;
	private ActivityMode mode;
	private String pgcrImage;

	ActivityIdentifier(String identifier, ActivityMode mode, String... hashes) {
		this.identifier = identifier;
		this.hashes = hashes;
		this.mode = mode;
	}

	ActivityIdentifier(String identifier, String pgcrImage, ActivityMode mode, String... hashes) {
		this.identifier = identifier;
		this.pgcrImage = pgcrImage;
		this.hashes = hashes;
		this.mode = mode;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getPgcrImage() { return pgcrImage; }

	/**
	 * Get the directorActivityHash of the Activity
	 * Can be used to compare different activities
	 */
	public String[] getHashes() {
		return hashes;
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

	public static ActivityIdentifier fromHash(String hash) {
		for(ActivityIdentifier activityIdentifier : ActivityIdentifier.values()) {
			for(String s : activityIdentifier.getHashes()) {
				if(s.equals(hash)) {
					return activityIdentifier;
				}
			}
		}

		return null;
	}
}
