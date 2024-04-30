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

    public boolean isOnline() {
        return isOnline;
    }

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

    public double getDaysSinceLastPlayed() {
        return StringUtils.getDaysSinceTime(getLastOnlineStatusChange());
    }

    public String getGroupId() {
        return groupId;
    }

    public Date getJoinDate() {
        return joinDate;
    }
}
