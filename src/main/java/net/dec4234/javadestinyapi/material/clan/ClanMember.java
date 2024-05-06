/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.clan;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.utils.StringUtils;

import java.util.Date;

/**
 * Represents a "ClanMember" which is basically the same as a BungieUser but with some added-on info like join date,
 * is online and last online date. This information is pulled from the /Members/ list of a clan.
 */
public class ClanMember extends BungieUser {

    private int memberType;
    private boolean isOnline;
    private String lastOnlineStatusChange, groupId;
    private Date joinDate, lastOnline;

    public ClanMember(JsonObject jsonObject) {
        super(jsonObject.getAsJsonObject("destinyUserInfo").get("membershipId").getAsString(), jsonObject.getAsJsonObject("destinyUserInfo"));
        memberType = jsonObject.get("memberType").getAsInt();
        isOnline = jsonObject.get("isOnline").getAsBoolean();
        lastOnlineStatusChange = jsonObject.get("lastOnlineStatusChange").getAsString();
        groupId = jsonObject.get("groupId").getAsString();

        joinDate = StringUtils.valueOfZTime(jsonObject.get("joinDate").getAsString());
    }

    public int getMemberType() {
        return memberType;
    }

    /**
     * @return True if this user is currently online in Destiny 2
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * When was the last time this user's online status changed. In other words, when they went from offline to online
     * or online to offline. If they are currently online then this could tell you how long they have been on in the
     * current playing session.
     * @return The date representing when their online status changed
     */
    public Date getLastOnlineStatusChange() {
        if(lastOnline == null) {
            try {
                lastOnline = new Date(Long.parseLong(lastOnlineStatusChange) * 1000);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return lastOnline;
    }

    /**
     * @return The number of days since this user's online status last changed
     */
    public double getDaysSinceLastPlayed() {
        return StringUtils.getDaysSinceTime(getLastOnlineStatusChange());
    }

    /**
     * @return The ID of the clan that this user belongs to
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return The date this user joined their current clan
     */
    public Date getJoinDate() {
        return joinDate;
    }
}
