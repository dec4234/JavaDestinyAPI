package net.dec4234.javadestinyapi.material.clan;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.utils.StringUtils;

import java.util.Date;

public class ClanMember {

    private int memberType;
    private boolean isOnline;
    private String lastOnlineStatusChange, groupId;
    private BungieUser bungieUser;
    private Date joinDate;

    public ClanMember(JsonObject jsonObject) {
        memberType = jsonObject.get("memberType").getAsInt();
        isOnline = jsonObject.get("isOnline").getAsBoolean();
        lastOnlineStatusChange = jsonObject.get("lastOnlineStatusChange").getAsString();
        groupId = jsonObject.get("groupId").getAsString();

        JsonObject destinyUserInfo = jsonObject.getAsJsonObject("destinyUserInfo");
        if (destinyUserInfo.has("membershipId")) {
            bungieUser = new BungieUser(destinyUserInfo.get("membershipId").getAsString(), destinyUserInfo);
        }

        joinDate = StringUtils.valueOfZTime(jsonObject.get("joinDate").getAsString());
    }

    public int getMemberType() {
        return memberType;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public Date getLastOnlineStatusChange() {
        try {
            return new Date(Long.parseLong(lastOnlineStatusChange) * 1000);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public BungieUser getBungieUser() {
        return bungieUser;
    }

    public Date getJoinDate() {
        return joinDate;
    }
}
