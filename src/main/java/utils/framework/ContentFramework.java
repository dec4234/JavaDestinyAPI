/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

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
