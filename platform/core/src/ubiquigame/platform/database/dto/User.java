package ubiquigame.platform.database.dto;

import com.google.gson.annotations.JsonAdapter;

public class User {

	private String username;

	@JsonAdapter(Base64Deserializer.class)
	private byte[] avatar;

	private int score;

	public String getName() {
		return username;
	}

	public void setName(String name) {
		this.username = name;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] encodedData) {
		this.avatar = encodedData;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
