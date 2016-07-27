package marko.mugloar;

public class Mugloar {
  static int wins = 0;
  static int losses = 0;
  static int battles = 0;

  public static void main(String[] args) {
  if (args.length == 0 ) {
      showHelp();
    }
    try {
      battles = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
        showHelp();
    }

    for (int i = 0; i < battles; i++) {
      BattleEngine battleEngine = new BattleEngine();
      battleEngine.battle();
    }
    printResult();
  }

  static void showHelp() {
    System.err.println("Usage: mugloar n");
    System.err.println("n - number of battles");
    System.exit(1);
  }

  public static void win() {
    wins++;
  }

  public static void defeat() {
    losses++;
  }

  static void printResult() {
    System.out.format("Games: %d \n", battles);
    System.out.format("Wins: %d \n", wins);
    System.out.format("Losses: %d \n", losses);
    System.out.format("Percentage : %d%%", (wins * 100 / battles));
  }
}
