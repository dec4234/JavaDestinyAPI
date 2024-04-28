/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.material.manifest.ManifestEntityTypes;

/**
 * Used as a standard framework for most stuff that bases its content off of a single request
 */
public class ContentFramework implements ContentInterface {

	private String url;
	private ManifestEntityTypes manifestType;
	protected JsonObject jo = null;
	private JsonObjectModifier jsonObjectModifier;

	public ContentFramework(JsonObject jsonObject) {
		this.jo = jsonObject;
		this.jsonObjectModifier = source -> source;
	}

	public ContentFramework(String url, JsonObjectModifier jsonObjectModifier) {
		this.url = url;
		this.jsonObjectModifier = jsonObjectModifier;
	}

	public ContentFramework(ManifestEntityTypes manifestType, String url, JsonObjectModifier jsonObjectModifier) {
		this.manifestType = manifestType;
		this.url = url;
		this.jsonObjectModifier = jsonObjectModifier;
	}

	@Override
	public void checkJO() throws APIException {
		if(jo == null) {
			if(manifestType == null) {
				jo = DestinyAPI.getHttpUtils().urlRequestGET(url);
			} else {
				jo = DestinyAPI.getHttpUtils().manifestGET(manifestType, url);
			}
		}
	}

	/**
	 * Refresh the jsonobject to potentially account for new changes
	 */
	public void refreshJO() throws APIException {
		if(manifestType == null) {
			jo = DestinyAPI.getHttpUtils().urlRequestGET(url);
		} else {
			jo = DestinyAPI.getHttpUtils().manifestGET(manifestType, url);
		}
	}

	public JsonObject getJO() throws APIException {
		checkJO();
		return jsonObjectModifier.modify(jo);
	}
}
