package sorryplspls.disguisable.properties;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import sorryplspls.disguisable.exceptions.SkinFetchException;
import sorryplspls.disguisable.config.ConfigService;
import sorryplspls.disguisable.requests.HttpRequesterService;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class TexturesService {

    private final HttpRequesterService httpRequesterService;

    public TexturesService(HttpRequesterService httpRequesterService) {
        this.httpRequesterService = httpRequesterService;
    }

    public Collection<ProfileProperty> skinTextures(String arg) {
        if (arg == null || arg.isEmpty()) {
            throw new SkinFetchException("Skin username is null");
        }

        String response = httpRequesterService.Request(ConfigService.getHttpRequest() + arg);

        if (response == null || response.isEmpty()) {
            throw new SkinFetchException("Failed to fetch user profile for: " + arg);
        }

        JsonObject object;
        try {
            object = JsonParser.parseString(response).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new SkinFetchException("Error parsing Mojang profile response.", e);
        }

        String uuid = Optional.ofNullable(object.get("id"))
                .map(JsonElement::getAsString)
                .orElseThrow(() -> new SkinFetchException("UUID not found for username: " + arg));

        String skinResponse = httpRequesterService.Request(ConfigService.getHttpAnswer().formatted(uuid));

        if (skinResponse == null || skinResponse.isEmpty()) {
            throw new SkinFetchException("Failed to fetch skin for UUID: " + uuid);
        }

        JsonObject skin;
        try {
            skin = JsonParser.parseString(skinResponse).getAsJsonObject();
        } catch (Exception e) {
            throw new SkinFetchException("Error parsing skin response.", e);
        }

        JsonObject propertyObject = Optional.ofNullable(skin.getAsJsonArray("properties"))
                .map(properties -> properties.get(0).getAsJsonObject())
                .orElseThrow(() -> new SkinFetchException("No properties found in skin response."));

        String value = Optional.ofNullable(propertyObject.get("value"))
                .map(JsonElement::getAsString)
                .orElseThrow(() -> new SkinFetchException("Texture value not found in skin response."));
        String signature = Optional.ofNullable(propertyObject.get("signature"))
                .map(JsonElement::getAsString)
                .orElseThrow(() -> new SkinFetchException("Texture signature not found in skin response."));

        return Collections.singletonList(new ProfileProperty("textures", value, signature));
    }

}
