package marko.mugloar;

public class Battle {
  private String gameId;
  private Knight knight;

  public String toSring() {
    return "GameId:"+ getGameId() + getKnight().toString();
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public Knight getKnight() {
    return knight;
  }

  public void setKnight(Knight knight) {
    this.knight = knight;
  }
}
