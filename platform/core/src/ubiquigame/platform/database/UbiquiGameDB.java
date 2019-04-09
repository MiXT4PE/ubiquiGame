package ubiquigame.platform.database;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ubiquigame.common.Player;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.database.dto.User;
import ubiquigame.platform.session.tournament.Score;

public class UbiquiGameDB {

	private static final String GET_ALL_USERS_ENDPOINT = PlatformConfiguration.API_HOST + PlatformConfiguration.API_URI
			+ "/getAllUsers.php";
	private static final String GET_USER_ENDPOINT = PlatformConfiguration.API_HOST + PlatformConfiguration.API_URI
			+ "/getUser.php";
	private static final String UPDATE_SCORE_ENDPOINT = PlatformConfiguration.API_HOST + PlatformConfiguration.API_URI
			+ "/updateScore.php";

	public UbiquiGameDB() {
	}

	public synchronized List<User> getAllUsers() {
		HttpGet getAllUsers = new HttpGet(GET_ALL_USERS_ENDPOINT);

		try (CloseableHttpClient httpClient = createSSLClient()) {
			CloseableHttpResponse response = httpClient.execute(getAllUsers);

			Gson gson = new Gson();
			HttpEntity entity = response.getEntity();
			InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
			JsonReader newJsonReader = gson.newJsonReader(inputStreamReader);
			User[] users = gson.fromJson(newJsonReader, User[].class);
			Comparator<User> comp = Comparator.comparingLong(user -> user.getScore());
			List<User> usersSorted = Arrays.asList(users);
			usersSorted.sort(comp.reversed());
			return usersSorted;

		} catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	private CloseableHttpClient createSSLClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true)
				.build();
		return HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
	}

	public void updateHighscore(Score totalScore) {
		for (Player player : totalScore.getPlayersRanked()) {
			updateScore(player.getName(), totalScore.get(player));
		}
	}

	private void updateScore(String playerName, int scoreToAdd) {
		try (CloseableHttpClient httpClient = createSSLClient()) {
			HttpPost updateScore = new HttpPost(UPDATE_SCORE_ENDPOINT);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", playerName));
			params.add(new BasicNameValuePair("scoreToAdd", Integer.toString(scoreToAdd)));
			updateScore.setEntity(new UrlEncodedFormEntity(params));

			httpClient.execute(updateScore);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Optional<User> getUser(String playerName) {
		try (CloseableHttpClient httpClient = createSSLClient()) {

			HttpPost getUser = new HttpPost(GET_USER_ENDPOINT);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", playerName));
			getUser.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse response = httpClient.execute( getUser);
			Gson gson = new Gson();
			HttpEntity entity = response.getEntity();
			InputStreamReader inputStreamReader = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
			JsonReader newJsonReader = gson.newJsonReader(inputStreamReader);
			User user = gson.fromJson(newJsonReader, User.class);
			return Optional.ofNullable(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}
}
