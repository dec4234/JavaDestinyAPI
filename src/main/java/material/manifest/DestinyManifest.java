/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package material.manifest;

import com.google.gson.JsonObject;
import utils.HttpUtils;
import utils.framework.ContentFramework;

import java.util.HashMap;

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
	public JsonObject manifestGET(ManifestEntityTypes manifestEntityTypes, String hash) {
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
	public String getVersion() {
		return getJO().get("version").getAsString();
	}

	public String getMobileAssetContentPath() {
		return getJO().get("mobileAssetContentPath").getAsString();
	}

	public String getMobileWorldContentPath(Language language) {
		return getJO().getAsJsonObject("mobileWorldContentPaths").get(language.getCode()).getAsString();
	}

	public String getJsonWorldContentPath(Language language) {
		return getJO().getAsJsonObject("jsonWorldContentPaths").get(language.getCode()).getAsString();
	}

	public JsonObject getWorldContent(Language language) {
		return new HttpUtils().urlRequestGET("https://www.bungie.net" + getJsonWorldContentPath(language));
	}

	/**
	 * Get the entirety of the specified definition library
	 */
	public JsonObject getDefinitionLibrary(ManifestEntityTypes manifestEntityTypes) {
		Language language = Language.ENGLISH;

		if(!worldComponents.containsKey(manifestEntityTypes.getBungieEntityValue())) {
			worldComponents.put(manifestEntityTypes.getBungieEntityValue(), new HttpUtils().urlRequestGET("https://www.bungie.net" + getJO().getAsJsonObject("jsonWorldComponentContentPaths").getAsJsonObject(language.getCode()).get(manifestEntityTypes.getBungieEntityValue()).getAsString()));
		}

		return worldComponents.get(manifestEntityTypes.getBungieEntityValue());
	}

	public JsonObject getDefinitionLibrary(Language language, ManifestEntityTypes manifestEntityTypes) {
		if(!worldComponents.containsKey(manifestEntityTypes.getBungieEntityValue())) {
			worldComponents.put(manifestEntityTypes.getBungieEntityValue(), new HttpUtils().urlRequestGET("https://www.bungie.net" + getJO().getAsJsonObject("jsonWorldComponentContentPaths").getAsJsonObject(language.getCode()).get(manifestEntityTypes.getBungieEntityValue()).getAsString()));
		}

		return worldComponents.get(manifestEntityTypes.getBungieEntityValue());
	}

	public String getMobileClanBannerDatabasePath() {
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
		PORTUGEUSE_BRAZIL("pt-br"),
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
