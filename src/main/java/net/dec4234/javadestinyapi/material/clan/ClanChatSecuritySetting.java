/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.clan;

public enum ClanChatSecuritySetting {

    GROUP(0),
    ADMINS(1);

    private int setting;

    private ClanChatSecuritySetting(int setting) {
        this.setting = setting;
    }

    public int getSetting() {
        return setting;
    }
}
