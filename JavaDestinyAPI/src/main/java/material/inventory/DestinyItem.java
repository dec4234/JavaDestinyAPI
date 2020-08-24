package material.inventory;

import com.google.gson.JsonObject;

public class DestinyItem {

	private String hashID;

	private JsonObject jo;

	public DestinyItem(String hashID) {
		this.hashID = hashID;
	}
}
