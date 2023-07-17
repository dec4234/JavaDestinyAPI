package net.dec4234.javadestinyapi.material.inventory.loadouts;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.material.inventory.items.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class Loadout {

    private String colorHash;
    private String nameHash;
    private String iconHash;

    private String characterID;

    private List<InventoryItem> items = new ArrayList<>();

    public Loadout(String characterID, String colorHash, String nameHash, String iconHash, List<InventoryItem> items) {
        this.colorHash = colorHash;
        this.nameHash = nameHash;
        this.iconHash = iconHash;
        this.items = items;
    }

    public Loadout(String characterID, JsonObject jsonObject) {

    }
}
