package net.dec4234.javadestinyapi.responses.user;

import com.google.gson.JsonObject;

import java.lang.reflect.Field;

/**
 * A class that represents a response from the Bungie API for a request for a list of sanitized usernames.
 * Potential responses will include a list of accounts that have been connected to this user's Bungie.net account.
 */
public class SanitizedUsernamesResponse {
    private JsonObject raw;
    private String SteamId; // capitalized because that's how it is in the JSON response
    private String TwitchId; // presumably there are more like this but I don't know what they are

    public SanitizedUsernamesResponse(JsonObject raw) {
        this.raw = raw;

        // If performance becomes a concern: https://github.com/Nesaak/NoReflection
        for(String key: raw.keySet()) { // trying out a new method of filling in fields automatically
            try {
                Field field = getClass().getDeclaredField(key);
                field.set(this, raw.get(key).getAsString());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public JsonObject getRaw() {
        return raw;
    }

    public String getSteamId() {
        return SteamId;
    }

    public String getTwitchId() {
        return TwitchId;
    }
}
