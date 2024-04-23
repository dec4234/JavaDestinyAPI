/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.clan.Clan;
import net.dec4234.javadestinyapi.material.user.BungieUser;
import net.dec4234.javadestinyapi.material.user.UserCredential;
import net.dec4234.javadestinyapi.material.user.UserCredentialType;
import net.dec4234.javadestinyapi.responses.user.SanitizedUsernamesResponse;
import net.dec4234.javadestinyapi.utils.HttpUtils;
import net.dec4234.javadestinyapi.utils.StringUtils;
import net.dec4234.javadestinyapi.utils.framework.OAuthManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DestinyAPI {

    private static String apiKey = null;
    private static String clientId = null;
    private static String clientSecret = null;
    private static String oauthCode = null;
    private static String accessToken = null;
    private static String refreshToken = null;

    private static OAuthManager oam = null;
    private static boolean debugEnabled = false;

    private static HttpUtils httpUtils;

    /**
     * Set the api key used by the DestinyAPI
     */
    public DestinyAPI setApiKey(String apiKey) {
        DestinyAPI.apiKey = apiKey;
        if (hasOauthManager()) {
            oam.setAPIToken(apiKey);
        }

        httpUtils = new HttpUtils(apiKey);

        return this;
    }

    public DestinyAPI setClientID(String clientId) {
        DestinyAPI.clientId = clientId;
        return this;
    }

    public DestinyAPI setClientSecret(String clientSecret) {
        DestinyAPI.clientSecret = clientSecret;
        return this;
    }

    public DestinyAPI setOauthCode(String oauthCode) {
        DestinyAPI.oauthCode = oauthCode;
        return this;
    }

    public DestinyAPI setAccessToken(String accessToken) {
        DestinyAPI.accessToken = accessToken;
        if (hasOauthManager()) {
            oam.setAccessToken(accessToken);
        }
        return this;
    }

    public DestinyAPI setRefreshToken(String refreshToken) {
        DestinyAPI.refreshToken = refreshToken;
        if (hasOauthManager()) {
            oam.setRefreshToken(refreshToken);
        }
        return this;
    }

    /**
     * Debug mode prints all requests and their responses to the console
     * This is very useful for feature development
     */
    public DestinyAPI enableDebugMode() {
        DestinyAPI.debugEnabled = true;

        return this;
    }

    /**
     * Disable debug mode
     */
    public DestinyAPI disableDebugMode() {
        DestinyAPI.debugEnabled = false;

        return this;
    }

    /**
     * Set the OAuth management class
     * The class passed in this parameter must "extends OAuthManager"
     */
    public DestinyAPI setOauthManager(OAuthManager oam) {
        DestinyAPI.oam = oam;
        // setApiKey(oam.getAPIToken());
        // setAccessToken(oam.getAccessToken());

        return this;
    }

    /**
     * Returns true if the OauthManager class has been defined
     */
    public static boolean hasOauthManager() {
        return oam != null;
    }


    /**
     * Returns a user based on the provided bungie ID
     *
     * @param id A bungie id such as "4611686018468620320"
     */
    public static BungieUser getUser(String id) {
        return new BungieUser(id);
    }

    public static SanitizedUsernamesResponse getSanitizedUsernames(String id) throws APIException {
        return new SanitizedUsernamesResponse(httpUtils.urlRequestGET(HttpUtils.URL_BASE + "/User/GetSanitizedPlatformDisplayNames/" + id + "/").getAsJsonObject("Response"));
    }

    /**
     * Get a BungieUser from a Steam ID
     * NOT the same IDs used by Bungie to identify individual users
     */
    public static BungieUser getMemberFromSteamID(String steamID) throws APIException {
        return getMemberFromPlatformID("SteamId", steamID);
    }

    /**
     * Used to get a BungieUser from a specified platform ID
     * Currently only works with Steam IDs (see getMemberFromSteamID())
     */
    private static BungieUser getMemberFromPlatformID(String platformName, String platformID) throws APIException {
        JsonObject jsonObject = getHttpUtils().urlRequestGET("https://www.bungie.net/Platform/User/GetMembershipFromHardLinkedCredential/" + platformName + "/" + platformID + "/").getAsJsonObject("Response");

        return new BungieUser(jsonObject.get("membershipId").getAsString());
    }

    /**
     * Bungie.net allows you to connect various other accounts to your own
     * Such as social media platforms and game platforms
     * <p>
     * Only usefulness at the moment would be to determine a Battle.net tag or Steam ID
     * <p>
     * Requires OAuth
     */
    public static UserCredential[] getUserCredentials(BungieUser bungieUser) throws APIException {
        List<UserCredential> list = new LinkedList<>();

        for (JsonElement je : getHttpUtils().urlRequestGETOauth("https://www.bungie.net/Platform/User/GetCredentialTypesForTargetAccount/" + bungieUser.getID() + "/").getAsJsonArray("Response")) {
            JsonObject jo = je.getAsJsonObject();

            if (jo.has("credentialDisplayName")) {
                if (jo.has("credentialAsString")) {
                    list.add(new UserCredential(UserCredentialType.fromPlatformCode(jo.get("credentialType").getAsInt()), jo.get("isPublic").getAsBoolean(), jo.get("credentialDisplayName").getAsString(), jo.get("credentialAsString").getAsString()));
                } else {
                    list.add(new UserCredential(UserCredentialType.fromPlatformCode(jo.get("credentialType").getAsInt()), jo.get("isPublic").getAsBoolean(), jo.get("credentialDisplayName").getAsString()));
                }
            } else {
                list.add(new UserCredential(UserCredentialType.fromPlatformCode(jo.get("credentialType").getAsInt()), jo.get("isPublic").getAsBoolean()));
            }
        }

        return list.toArray(new UserCredential[0]);
    }

    /**
     * Get a "UserCredential" from a BungieUser
     */
    public static UserCredential getUserCredential(UserCredentialType type, BungieUser bungieUser) throws APIException {
        for (UserCredential userCredential : getUserCredentials(bungieUser)) {
            if (userCredential.getUserCredentialType() == type) {
                return userCredential;
            }
        }

        return null;
    }

    /**
     * Searches for a user with the matching name and returns ONE BungieUser.
     * If a user has cross-saved to multiple platforms then they will have multiple
     * destiny membership profiles under their name.
     * <p>
     * Examines all membership profiles and will return a BungieUser with that destiny
     * profile if the cross save override is 0 or if it matches the membership type of that
     * profile.
     *
     * @param nameAndDiscrim A full name and discriminator such as "dec4234#9904"
     * @return A user with the matching name and discriminator, null otherwise
     */
    public static BungieUser getUserWithName(String nameAndDiscrim) throws APIException {
        try {
            String[] split = nameAndDiscrim.split("#");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("displayName", split[0]);
            jsonObject.addProperty("displayNameCode", split[1]);

            JsonObject response = getHttpUtils().urlRequestPOST(HttpUtils.URL_BASE + "/Destiny2/SearchDestinyPlayerByBungieName/-1/", jsonObject);

            return processListOfProfiles(response.getAsJsonArray("Response"));
        } catch (IndexOutOfBoundsException | NullPointerException e) { // If the name does not contain a # aka it is not properly formatted
            return null;
        }
    }

    /**
     * Search users across all platforms for anyone with that name.
     * <p>
     * Searching "dec4234" will return an array containing a single
     * BungieUser. While searching "Gladd" or "Datto" should return
     * multiple.
     */
    public static List<BungieUser> searchUsers(String name) throws APIException {
        List<BungieUser> users = new ArrayList<>();

        JsonObject body = new JsonObject();
        body.addProperty("displayNamePrefix", name);

        List<JsonObject> jsonObjects = new ArrayList<>();

        for (int i = 0; i < 100; i++) { // Start at page 0 and increment each time until there are no more valid pages
            JsonObject jsonObject = httpUtils.urlRequestPOST(HttpUtils.URL_BASE + "/User/Search/GlobalName/" + i + "/", body);

            // Only continue looping if the result has a list of search results
            if (jsonObject.has("Response") && jsonObject.getAsJsonObject("Response").getAsJsonArray("searchResults").size() != 0) {
                JsonArray jsonArray = jsonObject.getAsJsonObject("Response").getAsJsonArray("searchResults");

                for (JsonElement jsonElement : jsonArray) { // Add all user info objects into one list
                    jsonObjects.add(jsonElement.getAsJsonObject());
                }
            } else {
                break;
            }
        }

        // Process the one big list to convert bungie.net profile info into destiny profile info
        for (JsonObject jsonObject : jsonObjects) {
            BungieUser bungieUser = processListOfProfiles(jsonObject.getAsJsonArray("destinyMemberships"));

            if (bungieUser != null) {
                users.add(bungieUser);
            }
        }

        return users;
    }

    /**
     * "Process" a list of destiny membership profiles to identify which
     * should be used for the returned BungieUser
     * <p>
     * IGNORES all profiles associated with the provided credentials except for the most active! (When they are not crossaved)
     * Needs fixing, probably requires major overhall of the entire user system
     *
     * @param jsonArray
     * @return
     */
    private static BungieUser processListOfProfiles(JsonArray jsonArray) throws APIException {
        for (JsonElement jsonElement : jsonArray) {
            JsonObject profile = jsonElement.getAsJsonObject();

            String bungieId = profile.get("membershipId").getAsString();
            String displayName = profile.get("displayName").getAsString();
            int crossSaveOverride = profile.get("crossSaveOverride").getAsInt();
            int membershipType = profile.get("membershipType").getAsInt();
            boolean isPublic = profile.get("isPublic").getAsBoolean();

            BungieUser bungieUser = new BungieUser(bungieId, displayName, crossSaveOverride, membershipType, isPublic);

            /*
            Used when a user has accounts for multiple platforms connected to a Bungie.net account,
            but they are NOT cross saved. This means that the cross-save override is 0 and it would normally
            return the first in the list, often leading to issues. This is a temporary fix until the entire thing can be reworked.
            Returns the profile that has logged on most recently.
             */
            if (bungieUser.getCrossSaveOverride() == 0 && jsonArray.size() > 1) {
                JsonArray jsonArray1 = getHttpUtils().urlRequestGET("https://www.bungie.net/Platform/Destiny2/-1/Profile/" + bungieUser.getID() + "/LinkedProfiles/?components=200").getAsJsonObject("Response").getAsJsonArray("profiles");
                BungieUser toReturn = null;
                double days = 0;

                for (JsonElement jsonElement1 : jsonArray1) {
                    JsonObject jsonObject = jsonElement1.getAsJsonObject();

                    double newDays = StringUtils.getDaysSinceTime(StringUtils.valueOfZTime(jsonObject.get("dateLastPlayed").getAsString()));
                    if (toReturn == null || days > newDays) {
                        toReturn = new BungieUser(jsonObject.get("membershipId").getAsString());
                        days = newDays;
                    }
                }

                return toReturn;
            }

            if (bungieUser.getMembershipType() == bungieUser.getCrossSaveOverride() || bungieUser.getCrossSaveOverride() == 0) {
                return bungieUser;
            }
        }

        return null;
    }

    /**
     * You use this method to search for a user purely by their username
     * <p>
     * This method has been deprecated since 2/6/2022
     * Use searchUsers() instead
     */
    @Deprecated
    public static List<BungieUser> searchGlobalDisplayNames(String prefix) throws APIException {
        prefix = StringUtils.httpEncode(prefix);

        List<BungieUser> bungieUsers = new ArrayList<>();

        // Retrieve search results for the requested username prefix
        JsonArray jsonArray = getHttpUtils().urlRequestGET("https://www.bungie.net/Platform/User/Search/Prefix/" + prefix + "/0/").getAsJsonObject("Response").getAsJsonArray("searchResults");

        // Loop through all elements under the "searchResults" subsection
        for (JsonElement jsonElement : jsonArray) {
            List<JsonObject> destinyMemberships = new ArrayList<>(); // Create a list used to store all memebrship profiles for that user
            JsonObject jsonObject = jsonElement.getAsJsonObject(); // Get the entire user's subsection as a json object

            // LOOP through all membership profiles under the user's subsection
            for (JsonElement jsonElement1 : jsonObject.getAsJsonArray("destinyMemberships")) {
                destinyMemberships.add(jsonElement1.getAsJsonObject()); // Add profiles of the user to a list
            }

            BungieUser bungieUser = null;

            if (destinyMemberships.size() != 0) {
                JsonObject destinyMembership = null;

                if (destinyMemberships.size() == 1) {
                    destinyMembership = destinyMemberships.get(0);
                } else {
                    // Process resulting profile list to check for which one is the main one
                    for (JsonObject jsonObject1 : destinyMemberships) {
                        if (jsonObject1.get("crossSaveOverride").getAsInt() == jsonObject1.get("membershipType").getAsInt()) {
                            destinyMembership = jsonObject1;
                            break;
                        }
                    }
                }

                if (destinyMembership != null) {
                    bungieUser = new BungieUser(destinyMembership.get("membershipId").getAsString(), destinyMembership.get("displayName").getAsString(),
                            destinyMembership.get("bungieGlobalDisplayName").getAsString(), destinyMembership.get("crossSaveOverride").getAsInt(), destinyMembership.get("membershipType").getAsInt(),
                            destinyMembership.get("isPublic").getAsBoolean());
                }
            }

            if (bungieUser != null) {
                bungieUsers.add(bungieUser);
            }
        }


        return bungieUsers;
    }

    public static Clan getClan(long id) {
        return new Clan(id);
    }

    public static Clan getClan(String name) {
        return new Clan(name);
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static String getOauthCode() {
        return oauthCode;
    }

    public static String getAccessToken() {
        if (DestinyAPI.hasOauthManager()) {
            return oam.getAccessToken();
        }

        return accessToken;
    }

    public static String getRefreshToken() {
        if (DestinyAPI.hasOauthManager()) {
            return oam.getRefreshToken();
        }

        return refreshToken;
    }

    public static boolean isDebugEnabled() {
        return DestinyAPI.debugEnabled;
    }

    public static HttpUtils getHttpUtils() {
        return httpUtils;
    }

}
