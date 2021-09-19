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

	private String queryParameters = "empty";
	private volatile boolean hasQueryBeenReturned = false;

	public void initOAuthFlow(int port) {
		setTokens(port);
	}

	public void initOAuthFlowIfNeeded(int port) {
		if(!DestinyAPI.hasOauthManager() || DestinyAPI.getAccessToken() == null) {
			initOAuthFlow(8080);
		}
	}

	public void setTokens(int serverPort) {
		openOAuthPage();

		startSecureServer(serverPort);

		String rawCode = getRawCode(queryParameters);

		System.out.println(rawCode);

		DestinyAPI.getHttpUtils().setTokenViaAuth(rawCode);
	}

	public String getRawCode(String queryInput) {
		String codeString = queryInput.split("&")[0];

		return codeString.split("=")[1];
	}

	public void openOAuthPage() {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI("https://www.bungie.net/en/OAuth/Authorize?client_id=" + DestinyAPI.getClientId() + "&response_type=code"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	public void startSecureServer(int port) {
		HttpsServer server = null;
		final String[] queryParams = {""};

		SSLContext sslContext = null;
		try {
			server = HttpsServer.create(new InetSocketAddress(port), 0);
			sslContext = SSLContext.getInstance("TLS");
			char[] password = "mypassword".toCharArray();
			KeyStore ks = KeyStore.getInstance("JKS");
			FileInputStream fis = new FileInputStream(new File(Paths.get("").toAbsolutePath().toString()).getPath() + "\\keystore.jks");
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
	}

	public String getQueryParameters(HttpsExchange exchange) {
		queryParameters = exchange.getRequestURI().getQuery();
		hasQueryBeenReturned = true;

		return queryParameters;
	}

	@Deprecated
	public String startHttpServer(int port) {
		String response = null;

		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			boolean hasConnected = false;

			while (true) {
				Socket socket = serverSocket.accept();

				try {
					try (InputStream inputStream = socket.getInputStream()) { // ARM
						if (!hasConnected) {
							readInputHeaders(inputStream).forEach(s -> {
								System.out.println(s);
							});

							// String requestHeader = getHeaderToArray(raw).split("\n")[0].replace("GET ", "").replace(" HTTP/1.1", "");

							// System.out.println(requestHeader);

							String httpResponse = "HTTP/1.1 200 OK\r\n\r\n You can now close this window.";
							socket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));

							hasConnected = true;
						}
					}
				} catch (MalformedURLException ex) {
					System.err.println(socket.getLocalAddress() + " is not a parseable URL");
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
				}
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return response;
	}

	private List<String> readInputHeaders(InputStream inputStream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		List<String> headers = new ArrayList<>();

		while (true) {
			String s = null;
			try {
				s = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (s == null || s.trim().length() == 0) {
				break;
			}

			headers.add(s);
		}

		return headers;
	}
}
