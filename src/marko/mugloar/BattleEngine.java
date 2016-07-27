package marko.mugloar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.max;

public class BattleEngine {
  String weatherURL = "http://www.dragonsofmugloar.com/weather/api/report/";
  String gameURL = "http://www.dragonsofmugloar.com/api/game";
  String resultUrl = "http://www.dragonsofmugloar.com/api/game/{gameid}/solution";
  final String NORMAL = "NMR"; // normal
  final String STORM = "SRO"; //storm
  final String RAIN = "HVA"; //rain
  final String FOG = "FUNDEFINEDG";//fog
  final String DRY = "T E"; //dry

  public void battle() {
    try {
      Battle battle = getBattle();
      String weatherCode = getWeather(battle.getGameId());
      Dragon dragon = chooseDragon(weatherCode, battle.getKnight());
      String result = action(battle.getGameId(), dragon);
      if (result.contains("Dragon was successful") || result.contains("Dragon was zen enough to defeat the knight.") || result.contains("Knight was useless in the fog.") || result.contains("Dragon scratched up the umbrella boat and knight drowned.")) {
        Mugloar.win();
      } else {
        Mugloar.defeat();
      }
      System.out.println();
    }
    catch (Exception e) {
      System.err.println("Battle exception:" + e.toString());
    }

  }

  Battle getBattle() {
    String gameJsonContent = null;
//      gameJsonContent = "{\"gameId\":2861475,\"knight\":{\"name\":\"Sir. Caleb Bates of Quebec\",\"attack\":6,\"armor\":7,\"agility\":4,\"endurance\":3}}";
//      gameJsonContent = "{\"gameId\":1907438,\"knight\":{\"name\":\"Sir. Roger Elliott of Quebec\",\"attack\":4,\"armor\":5,\"agility\":3,\"endurance\":8}}";
//      gameJsonContent = "{\"gameId\":3086155,\"knight\":{\"name\":\"Sir. Andre Larson of Quebec\",\"attack\":5,\"armor\":4,\"agility\":7,\"endurance\":4}}";
//      gameJsonContent = "{\"gameId\":9732864,\"knight\":{\"name\":\"Sir. Vincent Kelly of Manitoba\",\"attack\":8,\"armor\":2,\"agility\":2,\"endurance\":8}}";
//      gameJsonContent = "{\"gameId\":4098083,\"knight\":{\"name\":\"Sir. Matthew Wolfe of Nova Scotia\",\"attack\":7,\"armor\":3,\"agility\":3,\"endurance\":7}}";
//     gameJsonContent = "{\"gameId\":5787612,\"knight\":{\"name\":\"Sir. Edgar Poole of Quebec\",\"attack\":2,\"armor\":8,\"agility\":2,\"endurance\":8}}";
    if (gameJsonContent == null) {
      try {
        URL url = new URL(gameURL);
        gameJsonContent = IOUtils.toString(new InputStreamReader(url.openStream()));
      }
      catch (Exception e) {
        System.err.println("Something went wrong when getting game!" + e.toString());
        System.exit(2);
      }
    }
    System.out.println(gameJsonContent);
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    return gson.fromJson(gameJsonContent, Battle.class);
  }

  protected String getWeather(String gameId) throws Exception {
    URL url = new URL(weatherURL + gameId);
    String weatherContentXML = IOUtils.toString(new InputStreamReader(url.openStream()));
    System.out.println(weatherContentXML);
    String weatherCode;
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

    DocumentBuilder builder = domFactory.newDocumentBuilder();
    Document dDoc = builder.parse(new InputSource(new StringReader(weatherContentXML)));
    XPath xPath = XPathFactory.newInstance().newXPath();
    weatherCode = (String) xPath.evaluate("/report/code", dDoc, XPathConstants.STRING);
    return weatherCode;
  }

  String action(String battleId, Dragon dragon) throws Exception {
    String jsonDragon = dragon.dragonToJson();
    String gamePutUrl = resultUrl.replace("{gameid}", battleId);

    URL url = new URL(gamePutUrl);
    URLConnection con = url.openConnection();
    HttpURLConnection http = (HttpURLConnection) con;
    http.setRequestMethod("PUT");
    http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    http.setDoOutput(true);

    byte[] out = jsonDragon.getBytes(StandardCharsets.UTF_8);
    http.setFixedLengthStreamingMode(out.length);

    try (OutputStream os = http.getOutputStream()) {
      os.write(out);
      os.close();
    }

    BufferedReader in = new BufferedReader(
      new InputStreamReader(
        http.getInputStream()));
    String response = IOUtils.toString(in);
    http.disconnect();
    System.err.println(response);

    return response;
  }

  Dragon chooseDragon(String weather, Knight knight) {
    int points = 20;

    Dragon dragon = new Dragon();
    switch (weather) {
      case RAIN:
        dragon = Dragon.rainDragon();
        break;
      case FOG:
        dragon = Dragon.fogDragon();
        break;
      case DRY:
      case STORM:
        dragon = Dragon.dryDragon();
        break;
      case NORMAL:
        dragon.setClawSharpness(distribute(knight.getArmor(), points, false));
        points = points - dragon.getClawSharpness();

        dragon.setScaleThickness(distribute(knight.getAttack(), points, (dragon.getClawSharpness() > 6)));
        points = points - dragon.getScaleThickness();

        dragon.setFireBreath(distribute(knight.getEndurance(), points, (max(dragon.getClawSharpness(), dragon.getScaleThickness())) > 6));
        points = points - dragon.getFireBreath();

        if (points <= 10) {
          dragon.setWingStrength(points);
        } else {
          dragon.setWingStrength(10);
          if (dragon.getClawSharpness() < 10) {
            dragon.setClawSharpness(dragon.getClawSharpness() + 1);
          } else {
            dragon.setScaleThickness(dragon.getScaleThickness() + 1);
          }
        }
        break;
      default:
        System.out.println("Found new weather type! >" + weather + "<");
        dragon = Dragon.dryDragon();
    }
    System.out.println(dragon.toString());
    return dragon;
  }

  int distribute(int knightStats, int toSpare, boolean epicUsed) {
    int result;
    int epic = 6;
    if (knightStats > epic) {
      if (!epicUsed) {
        result = knightStats + 2;
      } else {
        result = knightStats;
      }
    } else {
      if (knightStats < epic) {
        result = knightStats - 1;

      } else {
        result = knightStats;
      }
    }
    if (result < 0) {
      result = 0;
    }
    if (result > 10) {
      result = 10;
    }
    if (result > toSpare) {
      result = toSpare;
    }

    return result;
  }

}
