package net.dec4234.javadestinyapi.tests.testUtils;

import net.dec4234.javadestinyapi.material.DestinyAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * All test classes inside of JDA should use this file to easily manage API keys and other sensitive info.
 * To use, create a file and then pass it to {@link TestHandler#TestHandler(File)}. This will read the file and
 * initialize all relevant fields within the class, which will be passed to all tests.
 *
 * The file format should follow this format:
 * - It MUST have the first line as the API Key
 * - Lines 2 - 5 can either be entirely ommited or MUST be included all together
 * <br><br>
 * API Key (Required) <br>
 * Client ID (Group Optional) <br>
 * Client Secret (Group Optional) <br>
 * Access Token (Group Optional) <br>
 * Refresh Token (Group Optional) <br>
 * <br>
 * Each entry must be on a new line. If you don't want to include one of the optional fields just set it to "null"
 */
public class TestHandler {

    private String content;

    private String apiKey; // required
    private String clientId, clientSecret, accessToken, refreshToken; // optional
    public TestHandler(String filePath) throws IOException {
        this.content = Files.readString(Path.of(filePath));
        setFields();
        initializeDestinyAPI();
    }

    public TestHandler(File file) throws IOException {
        this.content = Files.readString(file.toPath());
        setFields();
        initializeDestinyAPI();
    }

    private void setFields() {
        if(content != null) {
            String[] list = content.split(System.lineSeparator());

            if(list.length == 5) {
                apiKey = list[0];
                clientId = list[1].equalsIgnoreCase("null") ? null : list[1];
                clientSecret = list[2].equalsIgnoreCase("null") ? null : list[2];
                accessToken = list[3].equalsIgnoreCase("null") ? null : list[3];
                refreshToken = list[4].equalsIgnoreCase("null") ? null : list[4];
            } else if (list.length == 1) {
                apiKey = list[0];
            } else {
                throw new IllegalArgumentException("Test file config must have 1 or 5 lines. Either just the API key " +
                        "or (API Key AND Client ID AND Client Secret AND Access Token AND Refresh Token), use null " +
                        "for empty fields if needed");
            }
        }
    }

    private void initializeDestinyAPI() {
        DestinyAPI destinyAPI = new DestinyAPI().setApiKey(apiKey);

        if(clientId != null && clientSecret != null) { // no point in separating them
            destinyAPI.setClientID(clientId);
            destinyAPI.setClientSecret(clientSecret);
        }

        // TODO: Implement OAuth section
        if(accessToken != null && refreshToken != null) {

        }
    }
}
