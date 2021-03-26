package utils.framework;

import com.google.gson.JsonObject;
import utils.HttpUtils;

public class ContentFramework implements ContentInterface {

	private String url;
	protected JsonObject jo = null;

	public ContentFramework(String url) {
		this.url = url;
	}

	@Override
	public void checkJO() {
		if(jo == null) {
			jo = new HttpUtils().urlRequestGET(url).getAsJsonObject("Response");
		}
	}
}
