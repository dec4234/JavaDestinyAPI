/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package jdaSrc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import material.DestinyAPI;
import utils.framework.OAuthManager;

import java.io.*;
import java.nio.file.Paths;

public class JavaDestinyAPIMain extends OAuthManager {

	// Paths.get("").toAbsolutePath().toString()).getPath() + "\\oauth.json"

	private File file = new File(Paths.get("").toAbsolutePath() + "\\oauth.json");
	private JsonObject jsonObject;

	public JavaDestinyAPIMain() {
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

