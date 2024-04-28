/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.material.manifest;

import com.google.gson.JsonObject;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.utils.framework.ContentFramework;

import java.util.HashMap;

/**
 * The Manifest is where you go to translate hashes commonly returned in longer requests to more specific information.
 * The Manifest is available in the most common languages, English, Spanish, etc.
 * Hashes are split into multiple categories such as InventoryItem and Perk categories and hashes must be appropriately
 * matched in order to get the response you want.
 */
public class DestinyManifest extends ContentFramework {

	private static HashMap<String, JsonObject> worldComponents = new HashMap<>();

	public DestinyManifest() {
		super("https://www.bungie.net/Platform/Destiny2/Manifest/", source -> {
			return source.getAsJsonObject("Response");
		});
	}

	/**
	 * A standard Manifest GET
	 * Unlike the manifestGET in HttpUtils(), this will not make a request every single time
	 *
	 * It'll download the entire definition library the first time and it'll cache it
	 */
	public JsonObject manifestGET(ManifestEntityTypes manifestEntityTypes, String hash) throws APIException {
		JsonObject jsonObject = getDefinitionLibrary(manifestEntityTypes);

		if(jsonObject.has(hash)) {
			return jsonObject.getAsJsonObject(hash);
		}

		return null;
	}

	/**
	 * Get the current version of the manifest
	 * Useful for checking for updates
	 */
	public String getVersion() throws APIException {
		return getJO().get("version").getAsString();
	}

	public String getMobileAssetContentPath() throws APIException {
		return getJO().get("mobileAssetContentPath").getAsString();
	}

	public String getMobileWorldContentPath(Language language) throws APIException {
		return getJO().getAsJsonObject("mobileWorldContentPaths").get(language.getCode()).getAsString();
	}

	public String getJsonWorldContentPath(Language language) throws APIException {
		return getJO().getAsJsonObject("jsonWorldContentPaths").get(language.getCode()).getAsString();
	}

	public JsonObject getWorldContent(Language language) throws APIException {
		return DestinyAPI.getHttpUtils().urlRequestGET("https://www.bungie.net" + getJsonWorldContentPath(language));
	}

	/**
	 * Get the entirety of the specified definition library
	 */
	public JsonObject getDefinitionLibrary(ManifestEntityTypes manifestEntityTypes) throws APIException {
		Language language = Language.ENGLISH;

		return getDefinitionLibrary(language, manifestEntityTypes);
	}

	public JsonObject getDefinitionLibrary(Language language, ManifestEntityTypes manifestEntityTypes) throws APIException {
		if(!worldComponents.containsKey(manifestEntityTypes.getBungieEntityValue())) {
			worldComponents.put(manifestEntityTypes.getBungieEntityValue(), DestinyAPI.getHttpUtils().urlRequestGET("https://www.bungie.net" + getJO().getAsJsonObject("jsonWorldComponentContentPaths").getAsJsonObject(language.getCode()).get(manifestEntityTypes.getBungieEntityValue()).getAsString()));
		}

		return worldComponents.get(manifestEntityTypes.getBungieEntityValue());
	}

	public String getMobileClanBannerDatabasePath() throws APIException {
		return getJO().get("mobileClanBannerDatabasePath").getAsString();
	}

	public enum Language {

		ENGLISH("en"),
		FRENCH("fr"),
		SPANISH("es"),
		SPANISH_MEXICO("es-mx"),
		GERMAN("de"),
		ITALIAN("it"),
		JAPANESE("ja"),
		PORTUGUESE_BRAZIL("pt-br"),
		RUSSIAN("ru"),
		KOREAN("ko"),
		ZH_CHT("zh-cht"),
		ZH_CHS("zh-chs");

		String code;

		private Language(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}
}
