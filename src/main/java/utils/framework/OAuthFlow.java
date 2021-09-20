/*
 * Copyright (c) dec4234 2021. Access is granted, without any express warranties or guarantees of
 * any kind,  to all wishing to use this software for their benefit. No one may specifically claim credit, or
 * ownership of this software without the explicit permission of the author.
 *
 * GitHub -> https://github.com/dec4234/JavaDestinyAPI
 */

package utils.framework;

import com.sun.net.httpserver.*;
import material.DestinyAPI;
import utils.StringUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class OAuthFlow {

	// keytool -genkey -dname "cn=dec 4234, ou=github/JavaDestinyAPI, o=ou=github/JavaDestinyAPI, c=US" -keyalg RSA -alias alias -keystore keystore.jks -storepass mypassword -keypass mypassword -validity 360 -keysize 2048

	private String queryParameters = "empty";
	private volatile boolean hasQueryBeenReturned = false;

	/**
	 * Initiate the OAuthFlow class which goes through the following steps
	 *
	 * 1. Opens the OAuth page on the user's default browser
	 * 2. Creates and HTTPS localhost server to receive that information
	 * 3. Extracts the oauth code from the query parameters
	 * 4.
	 * @param port
	 */
	public void initOAuthFlow(int port) {
		setTokens(port);
	}

	public void initOAuthFlowIfNeeded(int port) {
		if(!DestinyAPI.hasOauthManager() || DestinyAPI.getAccessToken() == null) {
			initOAuthFlow(port);
		}
	}

	private void setTokens(int serverPort) {
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

		SSLContext sslContext = null;
		try {
			server = HttpsServer.create(new InetSocketAddress(port), 0);
			sslContext = SSLContext.getInstance("TLS");
			char[] password = "mypassword".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");

			file.delete();

			// Generate a new key store
			// StringUtils.executeCommandLine("keytool -delete -alias alias -keystore keystore.jks");
			StringUtils.executeCommandLine("keytool -genkey -dname \"cn=dec 4234, ou=github/JavaDestinyAPI, o=ou=github/JavaDestinyAPI, c=US\" -ext san=dns:www.dev.dec4234.net -keyalg RSA -alias alias -keystore keystore.jks -storepass mypassword -keypass mypassword -validity 360 -keysize 2048");

			// Sleep to allow for the keystore to be generated
			Thread.sleep(1000);

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
		while(!hasQueryBeenReturned && time > System.currentTimeMillis()) {

		}

		server.stop(0);
		file.delete(); // Delete keystore
	}

	private String getQueryParameters(HttpsExchange exchange) {
		queryParameters = exchange.getRequestURI().getQuery();
		hasQueryBeenReturned = true;

		return queryParameters;
	}
}
