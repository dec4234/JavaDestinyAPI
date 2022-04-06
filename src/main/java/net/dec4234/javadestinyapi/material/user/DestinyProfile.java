package net.dec4234.javadestinyapi.material.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DestinyProfile {

    private String id, platformDisplayName, globalDisplayName;
    private int membershipType, crossSaveOverride, globalDisplayNameCode;
    private boolean isPublic;
    private List<Integer> applicableMembershipTypes;
    private Optional<Boolean> isCrossSavePrimary = Optional.empty(), isOverridden = Optional.empty();
    private Optional<String> lastSeenDisplayName = Optional.empty();
    private Optional<Integer> lastSeenDisplayNameType = Optional.empty();
    private Optional<Date> dateLastPlayed = Optional.empty();

    public DestinyProfile(JsonObject jsonObject) {
        id = jsonObject.get("membershipId").getAsString();
        membershipType = jsonObject.get("membershipType").getAsInt();

        platformDisplayName = jsonObject.get("displayName").getAsString();
        crossSaveOverride = jsonObject.get("crossSaveOverride").getAsInt();

        globalDisplayName = jsonObject.get("bungieGlobalDisplayName").getAsString();
        globalDisplayNameCode = jsonObject.get("bungieGlobalDisplayNameCode").getAsInt();

        isPublic = jsonObject.get("isPublic").getAsBoolean();

        List<Integer> ints = new ArrayList<>();

        for(JsonElement jsonElement : jsonObject.getAsJsonArray("applicableMembershipTypes")) {
            ints.add(jsonElement.getAsInt());
        }

        applicableMembershipTypes = ints;

        if(jsonObject.has("isOverridden")) {
            isOverridden = Optional.of(jsonObject.get("isOverridden").getAsBoolean());
        }

        if(jsonObject.has("isCrossSavePrimary")) {
            isCrossSavePrimary = Optional.of(jsonObject.get("isCrossSavePrimary").getAsBoolean());
        }

        if(jsonObject.has("dateLastPlayed")) {
            dateLastPlayed = Optional.ofNullable(StringUtils.valueOfZTime(jsonObject.get("dateLastPlayed").getAsString()));
        }

        if(jsonObject.has("LastSeenDisplayName")) {
            lastSeenDisplayName = Optional.of(jsonObject.get("LastSeenDisplayName").getAsString());
        }

        if(jsonObject.has("LastSeenDisplayNameType")) {
            lastSeenDisplayNameType = Optional.of(jsonObject.get("LastSeenDisplayNameType").getAsInt());
        }
    }

    /**
     * Returns the unqiue Bungie ID of this user
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the platform display name of this user
     * such as one from Steam or Xbox
     */
    public String getPlatformDisplayName() {
        return platformDisplayName;
    }

    /**
     * Returns the Global Display Name shared by all active profiles under this account
     */
    public String getGlobalDisplayName() {
        return globalDisplayName;
    }

    /**
     * Returns the membership type of this profile
     */
    public int getMembershipType() {
        return membershipType;
    }

    public int getCrossSaveOverride() {
        return crossSaveOverride;
    }

    public int getGlobalDisplayNameCode() {
        return globalDisplayNameCode;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<Integer> getApplicableMembershipTypes() {
        return applicableMembershipTypes;
    }

    public Optional<Boolean> getIsCrossSavePrimary() {
        return isCrossSavePrimary;
    }

    public Optional<Boolean> getIsOverridden() {
        return isOverridden;
    }

    public Optional<String> getLastSeenDisplayName() {
        return lastSeenDisplayName;
    }

    public Optional<Integer> getLastSeenDisplayNameType() {
        return lastSeenDisplayNameType;
    }
}
