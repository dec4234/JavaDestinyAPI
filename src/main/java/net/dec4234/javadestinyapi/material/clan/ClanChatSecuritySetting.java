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
