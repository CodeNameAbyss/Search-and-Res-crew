import java.util.Arrays;
//import java.util.Timer;
//import java.util.TimerTask;
/**
 * Commander class represents the leader of rescue operations
 * Extends Person to inherit basic person attributes
 * Responsible for allocating and managing rescue teams
 */
public class Commander extends Person {
  private static int points = 0;
  private static boolean gameOver = false;
  
  //Instance Variables

  //Constructors
  /**
   * Constructor for Commander
   * @param name - Array containing first name and optionally surname
   */
  public Commander(String[] name) {
    super(name);
  }

  //Accessors
  public static boolean getGameOver() {
    return gameOver;
  }
  public static int getPoints() {
    return points;
  }

  //Mutators
  public static void setPoints(int points) {
    Commander.points = points;
  }

  //Methods
  public static void reset() {
    points = 0;
    gameOver = false;
  }
  /**
   * Allocates resources
   */
  public static Boolean allocate(int responderID, int hazardID) {
    //Assign responders to hazards
    if (Main_runner.getTeam().searchResponders(responderID).getStatus().equals("Ready")) {
      if (!Main_runner.getIncidents().getHazardIDs().contains(hazardID)) {
        return null;
      }
      if (Main_runner.getTeam().searchResponders(responderID).getSpecialty().equals("reconnaissance"))
        Main_runner.getIncidents().searchHazards(hazardID).setKnown(Main_runner.getIncidents().searchHazards(hazardID).getKnown() + 2 + (int) (Math.random() * 3) - 1);
      else if (Main_runner.getTeam().searchResponders(responderID).getSpecialty().equals("investigator"))
        Main_runner.getIncidents().searchHazards(hazardID).setKnown(Main_runner.getIncidents().searchHazards(hazardID).getKnown() + 3 + (int) (Math.random() * 3) - 1);
      else if (Main_runner.getTeam().searchResponders(responderID).getSpecialty().equals("investigator"))
        Main_runner.getIncidents().searchHazards(hazardID).setKnown(Main_runner.getIncidents().searchHazards(hazardID).getKnown() + 1 + (int) (Math.random() * 3) - 1);
      Main_runner.getTeam().searchResponders(responderID).setStatus("Deployed");
      Main_runner.getIncidents().searchHazards(hazardID).addAssignee(responderID);
      return true;
    } else
    return false;
  }

  //Win and lose conditions and execution
  public static void checkGameOver() {
    //System.out.println(points + " " + Hazard.getFailedHazards());
    if (Hazard.getFailedHazards() >= 5) {
      System.out.println("\n\n" + Hazard.getFailedHazards() + " incident(s) were not addressed in time. You have been terminated Commander " + (Main_runner.getCommander().getSurname().equals("") ? Main_runner.getCommander().getName() : Main_runner.getCommander().getSurname()) +".\nEnd of simulation - Rebooting in 10 seconds...");
      /*Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          System.out.print("\033[H\033[2J");
          System.out.flush();
        };
      };
      timer.schedule(task, 1000 * 10);*/
      gameOver = true;
    } else if (points >= 10 + Main_runner.getDifficulty()) {
      System.out.println("\n\nCongratulations Commander " + (Main_runner.getCommander().getSurname().equals("") ? Main_runner.getCommander().getName() : Main_runner.getCommander().getSurname()) +"! You have successfully managed all incidents.\nEnd of simulation - Rebooting in 10 seconds...");
      /*Timer timer = new Timer();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          System.out.print("\033[H\033[2J");
          System.out.flush();
        };
      };
      timer.schedule(task, 1000 * 10);*/
      gameOver = true;
    }
  }

  //Capitalize the first letter of each word in a string
  public static String capitalize(String input) {
    StringBuilder str = new StringBuilder();
    for (String word : input.split(" ")) {
      if (word.substring(word.length()).equals(","))
        word = word.substring(0, word.length() - 1);
      str.append(!Arrays.asList(Responder.getTypes()).contains(word) ? word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " " : word.toUpperCase() + " ");
    }
    return str.toString().trim();
  }
}
