package marko.mugloar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Dragon {
  private int scaleThickness;
  private int clawSharpness;
  private int wingStrength;
  private int fireBreath;

  public int getScaleThickness() {
    return scaleThickness;
  }

  public void setScaleThickness(int scaleThickness) {
    this.scaleThickness = scaleThickness;
  }

  public int getClawSharpness() {
    return clawSharpness;
  }

  public void setClawSharpness(int clawSharpness) {
    this.clawSharpness = clawSharpness;
  }

  public int getWingStrength() {
    return wingStrength;
  }

  public void setWingStrength(int wingStrength) {
    this.wingStrength = wingStrength;
  }

  public int getFireBreath() {
    return fireBreath;
  }

  public void setFireBreath(int fireBreath) {
    this.fireBreath = fireBreath;
  }
  public String dragonToJson() {
    final GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(Dragon.class, new DragonSerializer());
    gsonBuilder.setPrettyPrinting();
    final Gson gson = gsonBuilder.create();
    return gson.toJson(this);
  }

  public static Dragon rainDragon() {
    Dragon dragon = new Dragon();
    dragon.setClawSharpness(10);
    dragon.setScaleThickness(5);
    dragon.setFireBreath(0);
    dragon.setWingStrength(5);
    return dragon;
  }
  public static Dragon fogDragon() {
    Dragon dragon = new Dragon();
    dragon.setClawSharpness(4);
    dragon.setScaleThickness(4);
    dragon.setFireBreath(7);
    dragon.setWingStrength(5);
    return dragon;
  }
  public static Dragon dryDragon() {
    Dragon dragon = new Dragon();
    dragon.setClawSharpness(5);
    dragon.setScaleThickness(5);
    dragon.setFireBreath(5);
    dragon.setWingStrength(5);
    return dragon;
  }


  @Override
  public String toString() {
    return "scaleThickness:" +scaleThickness + " clawSharpness:"+ clawSharpness + " wingStrength:" + wingStrength + " fireBreath:" + fireBreath;
  }

}
