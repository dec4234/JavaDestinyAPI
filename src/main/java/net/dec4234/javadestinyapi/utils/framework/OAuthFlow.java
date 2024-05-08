/*
 * Copyright (c) 2024. dec4234
 * A standard open MIT license applies. Modififcation and usage permitted with credit. No warranties or express guarentees are given in any way.
 *
 * Github -> https://github.com/dec4234/JavaDestinyAPI
 */

package net.dec4234.javadestinyapi.utils.framework;

import com.sun.net.httpserver.*;
import net.dec4234.javadestinyapi.exceptions.APIException;
import net.dec4234.javadestinyapi.exceptions.InvalidConditionException;
import net.dec4234.javadestinyapi.material.DestinyAPI;
import net.dec4234.javadestinyapi.utils.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.concurrent.Executors;

/**
 * The OAuthFlow class allows a user to easily generate oauth tokens for a small project that is not distributed to
 * external users. This class could be replicated by an experienced user to allow for handling multiple oauth tokens.
 * <p>
 * Note: OAuth could allow potentially dangerous actions such as full control over your clan (if you are an admin) as
 * well as your inventory. Use at your own risk, and use good data management and protection practices.
 * <p>
 * Here is the process: <br>
 * 1. Go to your app on <a href="https://www.bungie.net/en/Application">https://www.bungie.net/en/Application</a><br>
 * 2. Under "App authentication" set the client type to confidential and set the redirect to something like
 * "https://localhost:8080". Note that it MUST be HTTPS not HTTP<br>
 * 3. Adjust the permissions for the app using the checkboxes right below it.<br>
 * 4. Use the following code to init your oauth tokens.
 * <pre>{@code
 * OAuthFlow oAuthFlow = new OAuthFlow();
 * oAuthFlow.initOAuthFlowIfNeeded(8080);
 * }</pre>
 * This will open the browser on your local machine where it will direct you to do oauth. It might say that the site is
 * unsafe since it doesn't have a valid certificate. On chrome, click the "advanced" button and then "continue". You
 * should now be able to perform OAuth requests and all the interesting things you can build with it.<br>
 * 5.(Optionally) When initializing the DestinyAPI you can use {@link DestinyAPI#setOauthManager(OAuthManager)} to save
 * the tokens however you want, so you don't have to oauth frequently.
 */
public class OAuthFlow {

	// keytool -genkey -dname "cn=dec 4234, ou=github/JavaDestinyAPI, o=ou=github/JavaDestinyAPI, c=US" -keyalg RSA -alias alias -keystore keystore.jks -storepass mypassword -keypass mypassword -validity 360 -keysize 2048

	private String queryParameters = "empty";
	private volatile boolean hasQueryBeenReturned = false;

	/**
	 * Initiate the OAuthFlow class which goes through the following steps
	 *
	 * 1. Opens the OAuth page on the user's default browser
	 * 2. Creates an HTTPS localhost server to receive that information
	 * 3. Extracts the oauth code from the query parameters
	 * 4. Sets the tokens using that information
	 * @param port The port to start the server on
	 */
	public void initOAuthFlow(int port) throws APIException {
		setTokens(port); //TODO: bug: waits 30s for no reason? not part of this class?
	}

	/**
	 * Initiate the OAuth Flow only if an existing key cannot be found or if it has expired
	 * @param port The port to start the server on
	 */
	public void initOAuthFlowIfNeeded(int port) throws APIException {
		if(DestinyAPI.getHttpUtils().hasValidOAuthTokens()) {
			return;
		}

		try {
			if(!DestinyAPI.hasOauthManager() || DestinyAPI.getAccessToken() == null || DestinyAPI.getHttpUtils().setTokenViaRefresh() == null) {
				initOAuthFlow(port);
			}
		} catch (InvalidConditionException e) {
			initOAuthFlow(port);
		}
	}

	private void setTokens(int serverPort) throws APIException {
		openOAuthPage();

		startSecureServer(serverPort);

		String rawCode = getRawCode(queryParameters);

		DestinyAPI.getHttpUtils().setTokenViaAuth(rawCode);
	}

	private String getRawCode(String queryInput) {
		String codeString = queryInput.split("&")[0];

		return codeString.split("=")[1];
	}

	private void openOAuthPage() {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.bungie.net/en/OAuth/Authorize?client_id=" + DestinyAPI.getClientId() + "&response_type=code"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	private void startSecureServer(int port) {
		HttpsServer server = null;
		final String[] queryParams = {""};
		String filePath = new File(Paths.get("").toAbsolutePath().toString()).getPath() + "\\keystore.jks";
		File file = new File(filePath);

		try {
			SSLContext sslContext = null;
			try {
				server = HttpsServer.create(new InetSocketAddress(port), 0);
				sslContext = SSLContext.getInstance("TLS");
				char[] password = "mypassword".toCharArray();
				KeyStore ks = KeyStore.getInstance("JKS");

				file.delete();

				// Generate a new key store
				StringUtils.executeCommandLine("keytool -genkey -dname \"cn=dec 4234, ou=github/JavaDestinyAPI, o=ou=github/JavaDestinyAPI, c=US\" -ext san=dns:www.dev.dec4234.net -keyalg RSA -alias alias -keystore keystore.jks -storepass mypassword -keypass mypassword -validity 360 -keysize 2048");

				// Sleep to allow for the keystore to be generated
				Thread.sleep(2000);

				FileInputStream fis = new FileInputStream(filePath);
				ks.load(fis, password);

				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, password);

				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				tmf.init(ks);

				sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}

			HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext) {
				@Override
				public void configure(HttpsParameters httpsParameters) {
					SSLContext sslContext = getSSLContext();
					SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
					httpsParameters.setSSLParameters(defaultSSLParameters);
				}
			};

			server.createContext("/", t -> {
				HttpsExchange s = (HttpsExchange) t;
				s.getSSLSession();

				String response = "<html><body>You can now close this page.</body></html>";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());

				getQueryParameters(s);

				os.close();

				s.close();
			});

			long time = System.currentTimeMillis() + (30 * 1000);

			server.setExecutor(Executors.newCachedThreadPool());
			server.setHttpsConfigurator(httpsConfigurator);
			server.start();

			// A crude attempt at a time-out
			while (!hasQueryBeenReturned && time > System.currentTimeMillis()) {

			}
		} finally {
			if(server != null) {
				server.stop(0);
			}
			file.delete(); // Delete keystore
		}
	}

	/**
	 * Return the query parameters of a particular incoming request
	 */
	private String getQueryParameters(HttpsExchange exchange) {
		queryParameters = exchange.getRequestURI().getQuery();
		hasQueryBeenReturned = true;

		return queryParameters;
	}
}
