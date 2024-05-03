/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.utils.framework.OAuthManager;

import java.io.*;
import java.nio.file.Paths;

/**
 * This class is provided for convenience for local apps using OAuth. It is also provided as a template for what
 * you should do to implement your own token manager.
 * <br>
 * The author assumes no responsibility for the use of this class. It is unsafe to use this in a production environment,
 * proceed with your own risk.
 */
public class JsonOAuthManager extends OAuthManager {

	private File file = new File(Paths.get("").toAbsolutePath() + "\\oauth.json");
	private JsonObject jsonObject;

	public JsonOAuthManager() {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			try {
				jsonObject = new JsonParser().parse(new FileReader(file.getAbsolutePath())).getAsJsonObject();
			} catch (IllegalStateException exception) { // If the file is empty or corrupted
				jsonObject = new JsonObject();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAccessToken() {
		if(jsonObject.has("access-token")) {
			return jsonObject.get("access-token").getAsString();
		}

		return null;
	}

	@Override
	public String getRefreshToken() {
		if(jsonObject.has("refresh-token")) {
			return jsonObject.get("refresh-token").getAsString();
		}

		return null;
	}

	@Override
	public String getAPIToken() {
		return DestinyAPI.getApiKey();
	}

	@Override
	public void setAccessToken(String accessToken) {
		jsonObject.addProperty("access-token", accessToken);
		save();
	}

	@Override
	public void setRefreshToken(String refreshToken) {
		jsonObject.addProperty("refresh-token", refreshToken);
		save();
	}

	@Override
	public void setAPIToken(String apiToken) {

	}

	public void save() {
		try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath())) {
			fileWriter.write(jsonObject.toString());
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

