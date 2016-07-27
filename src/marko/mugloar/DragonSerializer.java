package marko.mugloar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class DragonSerializer implements JsonSerializer<Dragon> {
  @Override
  public JsonElement serialize(Dragon dragon, final Type typeOfSrc, final JsonSerializationContext context) {
    final JsonObject jsonDragonStats = new JsonObject();
    jsonDragonStats.addProperty("scaleThickness", dragon.getScaleThickness());
    jsonDragonStats.addProperty("clawSharpness", dragon.getClawSharpness());
    jsonDragonStats.addProperty("wingStrength", dragon.getWingStrength());
    jsonDragonStats.addProperty("fireBreath", dragon.getFireBreath());
    final JsonObject jsonDragon = new JsonObject();
    jsonDragon.add("dragon", jsonDragonStats);
    return jsonDragon;
  }
}
