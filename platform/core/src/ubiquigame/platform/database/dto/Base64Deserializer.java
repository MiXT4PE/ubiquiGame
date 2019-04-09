package ubiquigame.platform.database.dto;

import java.lang.reflect.Type;
import java.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class Base64Deserializer implements JsonDeserializer<byte[]> {

	@Override
	public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String base64Enc = json.getAsString();
		return Base64.getDecoder().decode(base64Enc);

	}

}
