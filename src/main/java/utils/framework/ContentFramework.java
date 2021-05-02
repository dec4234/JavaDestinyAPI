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

/**
 * Used as a standard framework for most stuff that bases its content off of a single request
 */
public class ContentFramework implements ContentInterface {

	private String url;
	private JsonObject jo = null;
	private JsonObjectModifier jsonObjectModifier;

	public ContentFramework(String url, JsonObjectModifier jsonObjectModifier) {
		this.url = url;
		this.jsonObjectModifier = jsonObjectModifier;
	}

	@Override
	public void checkJO() {
		if(jo == null) {
			jo = jsonObjectModifier.modify(new HttpUtils().urlRequestGET(url));
		}
	}

	public JsonObject getJO() {
		checkJO();
		return jo;
	}
}
