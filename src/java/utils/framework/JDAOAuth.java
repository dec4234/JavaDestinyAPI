package utils.framework;

public interface JDAOAuth {

	String getAccessToken();
	String getRefreshToken();
	String getAPIToken();

	void setAccessToken(String accessToken);
	void setRefreshToken(String refreshToken);
	void setAPIToken(String apiToken);
}
