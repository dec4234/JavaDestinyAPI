/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.social;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;

import java.util.ArrayList;
import java.util.List;

import static net.dec4234.javadestinyapi.utils.HttpUtils.URL_BASE;

public class SocialFriendsList {

    public List<SocialFriend> getFriendsList() throws APIException {
        List<SocialFriend> list = new ArrayList<>();

        JsonObject jo = DestinyAPI.getHttpUtils().urlRequestGETOauth(URL_BASE + "/Social/Friends/");

        if(!jo.has("Response")) {
            return null;
        }

        for(JsonElement jsonElement : jo.getAsJsonObject("Response").getAsJsonArray("friends")) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            list.add(new SocialFriend(jsonObject));
        }

        return list;
    }
}
