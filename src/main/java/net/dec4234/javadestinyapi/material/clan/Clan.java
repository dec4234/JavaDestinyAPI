/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.clan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.stats.activities.ActivityMode;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.StringUtils;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Clan extends ContentFramework {

    private HttpUtils hu = DestinyAPI.getHttpUtils();

    private long clanId = -1;
    private String clanName, clanDescription, motto;

    // Details about the clan (more or less in the order they appear in the JSON response)
    private Date creationDate;
    private int memberCount = -1;

    private ClanMember founder;
    private List<ClanMember> admins, members;
    private ClanManagement clanManagement;
    private JsonObject jj, detail, founderJO;

    /**
     * @param clanId The ID of the clan
     */
    public Clan(long clanId) {
        super("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200", source -> {
            return source.getAsJsonObject("Response");
        });
        this.clanId = clanId;
    }

    /**
     * This is no longer the preferred method for searching by name. Use {@link DestinyAPI#searchClan(String)}
     * @param clanName The name of the clan that you would like to look at
     */
    @Deprecated
    public Clan(String clanName) {
        super(("https://www.bungie.net/Platform/GroupV2/Name/" + StringUtils.httpEncode(clanName) + "/1/?components=200"), source -> {
            return source.getAsJsonObject("Response");
        });
        this.clanName = clanName;
    }

    public Clan(long clanId, String clanName) {
        super(("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200"), source -> {
            return source.getAsJsonObject("Response");
        });
        this.clanId = clanId;
        this.clanName = clanName;
    }

    public Clan(long clanId, JsonObject detail, JsonObject founder) {
        super(("https://www.bungie.net/platform/GroupV2/" + clanId + "/?components=200"), source -> {
            return source.getAsJsonObject("Response");
        });
        this.clanId = clanId;
        this.detail = detail;
        this.founderJO = founder;
    }

    public String getClanID() throws APIException {
        if (clanId == -1) {
            clanId = getDetail().get("groupId").getAsLong();
        }
        return clanId + "";
    }

    public String getClanName() throws APIException {
        if (clanName == null) {
            clanName = getJO().getAsJsonObject("detail").get("name").getAsString();
        }
        return clanName;
    }

    public String getClanDescription() throws APIException {
        if (clanDescription == null) {
            clanDescription = getDetail().get("about").getAsString();
        }
        return clanDescription;
    }

    public Date getCreationDate() throws APIException {
        if (creationDate == null) {
            creationDate = StringUtils.valueOfZTime(getDetail().get("creationDate").getAsString());
        }
        return creationDate;
    }

    public int getMemberCount() throws APIException {
        if (memberCount == -1) {
            memberCount = getDetail().get("memberCount").getAsInt();
        }
        return memberCount;
    }

    public boolean isPublic() throws APIException {
        return getDetail().get("isPublic").getAsBoolean();
    }

    public String getMotto() throws APIException {
        if (motto == null) {
            motto = getDetail().get("motto").getAsString();
        }
        return motto;
    }

    /**
     * Get if this clan allows chat. I think this refers to the Bungie.net clan chat page
     */
    public boolean isAllowChat() throws APIException {
        return getDetail().get("allowChat").getAsBoolean();
    }

    /**
     * Get the founder of the clan
     */
    public ClanMember getFounder() throws APIException {
        if(founder != null) {
            return new ClanMember(founderJO);
        }

        return new ClanMember(getJO().getAsJsonObject("founder"));
    }

    /**
     * Returns a list of the founder and the admins of the clan.
     * The founder is always the first in this list?
     * Followed by the admins in the order they were promoted
     */
    public List<ClanMember> getAdmins() throws APIException {
        if (admins != null) {
            return admins;
        }

        List<ClanMember> temp = new ArrayList<>();
        JsonArray ja = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/AdminsAndFounder/?components=200").getAsJsonObject("Response").getAsJsonArray("results");

        for (JsonElement je : ja) {
            temp.add(new ClanMember(je.getAsJsonObject()));
        }

        admins = temp; // Cache this information
        return temp;
    }

    /**
     * @return A double representing the average amount of days since clan members have last logged in.
     */
    public double getAverageInactivityAmongMembers() throws APIException {
        ArrayList<Double> averages = new ArrayList<>();
        int a = 0;
        for (ClanMember clanMember : this.getMembers()) {
            averages.add(clanMember.getDaysSinceLastPlayed());
        }
        for (Double d : averages) {
            a += d;
        }
        return (double) a / getMembers().size();
    }

    /**
     * Get the most inactive members of a clan, using the getMembers() endpoint and the ClanMember class
     * @param numberOfResults The number of inactive members you want
     * @param exclude An exclusion list of bungie IDs. If an inactive user has their id in this list then they will NOT
     *                be included in the returned list.
     * @return A list of the most inactive members, sorted from most to least inactive
     */
    public List<ClanMember> getMostInactiveMembers(int numberOfResults, String... exclude) throws APIException {
        List<ClanMember> list = getMembers();
        List<String> excluded = Arrays.asList(exclude);
        List<ClanMember> toReturn = new LinkedList<>();

        for (int i = 0; i < numberOfResults; i++) {
            ClanMember temp = null;

            for (ClanMember clanMember : list) {
                if(temp != clanMember && !toReturn.contains(clanMember) && !excluded.contains(clanMember.getID())) {
                    if (temp == null || (clanMember.getLastOnlineStatusChange().getTime() != 0 && clanMember.getDaysSinceLastPlayed() > temp.getDaysSinceLastPlayed())) {
                        temp = clanMember;
                    }
                }
            }

            toReturn.add(temp);
        }

        return toReturn;
    }

    /**
     * Search for all of the members in this clan that have the string in their name
     *
     * TO-DO: Maybe switch this to the search part of the /Members/ endpoint?
     */
    public List<BungieUser> searchMembers(String name) throws APIException {
        List<BungieUser> list = new LinkedList<>();

        for (BungieUser bungieUser : getMembers()) {
            if (bungieUser.getGlobalDisplayName().contains(name)) {
                list.add(bungieUser);
            }
        }

        return list;
    }

    /**
     * Gets all presently online members of the clan
     * Returns very quickly because it only needs 1 request
     *
     * @return A list of all online members of the clan
     */
    public List<ClanMember> getOnlineMembers() throws APIException {
        List<ClanMember> toReturn = new ArrayList<>();

        for(ClanMember clanMember : getMembers()) {
            if(clanMember.isOnline()) {
                toReturn.add(clanMember);
            }
        }

        return toReturn;
    }

    /**
     * Only get the user ids of the members of this clan instead of BungieUsers
     *
     * @return The userIds belonging to the members of this clan
     */
    public List<String> getMembersIDs() throws APIException {
        List<String> toReturn = new ArrayList<>();

        for (BungieUser bungieUser : getMembers()) {
            toReturn.add(bungieUser.getID());
        }

        return toReturn;
    }

    /**
     * Get the members of this clan
     * @return A List of ClanMember
     */
    public List<ClanMember> getMembers() throws APIException {
        List<ClanMember> clanMembers = new ArrayList<>();

        JsonObject response = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/Members/").get("Response").getAsJsonObject();

        for (JsonElement jsonElement : response.getAsJsonArray("results")) {
            clanMembers.add(new ClanMember(jsonElement.getAsJsonObject()));
        }

        return clanMembers;
    }

    /**
     * Get the oldest members of this clan, sorted from the first person to join to most recently joined
     * @param amount The amount of members to return
     * @return A sorted list of the oldest members of this clan
     */
    public List<ClanMember> getOldestMembers(int amount) throws APIException {
        List<ClanMember> members = getMembers();
        List<ClanMember> sorted = new LinkedList<>();

        ClanMember oldest = null;

        for(int i = 0; i < amount; i++) {
            if(i > members.size()) {
                break;
            }

            for(ClanMember inner : members) {
                if(inner != oldest && !sorted.contains(inner)) {
                    if(oldest == null || inner.getJoinDate().getTime() < oldest.getJoinDate().getTime()) {
                        oldest = inner;
                    }
                }
            }

            sorted.add(oldest);
            oldest = null;
        }

        return sorted;
    }

    /**
     * Returns if this BungieUser is a member of the clan
     */
    public boolean isMember(BungieUser bungieUser) throws APIException {
        return isMember(bungieUser.getID());
    }

    /**
     * Checks if the member with the provided id is a member of the clan
     */
    public boolean isMember(String bungieID) throws APIException {
        for (BungieUser bungieUser : getMembers()) {
            if (bungieUser.getID().equals(bungieID)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieve a JsonObject depicting the top stats of the clan
     * Unfortunately does not say who has those top stats
     */
    public JsonObject getClanStats(ActivityMode... filter) throws APIException {
        String queryString = "/?modes=";
        for (ActivityMode activityMode : filter) {
            queryString = queryString.concat(activityMode.getBungieValue() + ",");
        }
        queryString = queryString.substring(0, queryString.length() - 2); // Remove the last comma

        return hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/Stats/AggregateClanStats/" + getClanID() + queryString);
    }

    /**
     * Get the date that this user joined the clan
     *
     * @return The Date this user joined the clan or null if that user was not found
     */
    public Date getJoinDate(BungieUser member) throws APIException {
        if (jj == null) {
            jj = hu.urlRequestGET("https://www.bungie.net/Platform/GroupV2/" + getClanID() + "/Members/").get("Response").getAsJsonObject();
        }

        for (JsonElement je : jj.getAsJsonArray("results")) {
            if (member.getID().equals(je.getAsJsonObject().getAsJsonObject("destinyUserInfo").get("membershipId").getAsString())) {
                return StringUtils.valueOfZTime(je.getAsJsonObject().get("joinDate").getAsString());
            }
        }

        return null; // Return null if there were no matching users found
    }

    /**
     * Get the management class for this clan.
     */
    public ClanManagement getClanManagement() {
        if (clanManagement != null) {
            return clanManagement;
        }
        clanManagement = new ClanManagement(this);
        return clanManagement;
    }

    private JsonObject getDetail() throws APIException {
        if(detail != null) {
            return detail;
        }

        return getJO().getAsJsonObject("detail");
    }
}
