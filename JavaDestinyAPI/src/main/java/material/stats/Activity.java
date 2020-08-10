package material.stats;

import com.google.gson.JsonObject;
import utils.HttpUtils;

public class Activity {

	HttpUtils hu = new HttpUtils();
	String activityId;
	JsonObject jo;

	public Activity(String activityId) {
		this.activityId = activityId;
		jo = hu.urlRequestGET("https://www.bungie.net/Platform/Destiny2/Stats/PostGameCarnageReport/" + activityId + "/");
	}

	public JsonObject getJsonObject() {
		return jo;
	}
}
