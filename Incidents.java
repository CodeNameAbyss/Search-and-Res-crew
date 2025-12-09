import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Incidents class represents an emergency incident/scenario
 * Contains lists of hazards present and people requiring rescue
 */
public class Incidents {
  //Instance Variables
  private ArrayList<Hazard> hazards; // List of hazards present in this incident
  private Timer timer;
  private TimerTask task;
  private static boolean start = true;
  //Constructors
  /**
   * Default constructor - creates an incident with empty lists
   */
  public Incidents() {
    this(new ArrayList<Hazard>());
  }
  /**
   * Parameterized constructor for Incidents
   * @param hazards - List of hazards in this incident
   */
  public Incidents(ArrayList<Hazard> hazards) {
    this.hazards = new ArrayList<Hazard>();
    this.timer = new Timer();
  }

  //Accessors
  /**
   * Gets the list of hazards in this incident
   * @return ArrayList of hazards
   */
  public ArrayList<Hazard> getHazards() {
    return hazards;
  }
  public ArrayList<Integer> getHazardIDs() {
    ArrayList<Integer> ids = new ArrayList<Integer>();
    for (Hazard h : hazards) {
      ids.add(h.getID());
    }
    return ids;
  }
  public static boolean getStart() {
    return start;
  }
  //Mutators
  /**
   * Sets the list of hazards for this incident
   * @param hazards - New list of hazards
   */
  public void addHazard() {
    this.hazards.add(new Hazard());
  }
  public boolean removeHazards(Hazard hazard) {
    return this.hazards.remove(hazard);
  }
  public static void setStart(boolean start) {
    Incidents.start = start;
  }
  

  //Methods
  public void startTimer() {
    timer.schedule(task = new TimerTask() {
    @Override
    public void run() {
        if (Main_runner.getIncidents().getHazards().size() == 0)
          System.out.println("\n\n\t!An incident has been reported!\n");
        addHazard();
        startTimer();
      }
    }, 1000 * ((int) (Math.random() * 4) == 0 ? (int) (Math.random() * 30 + 1) : ((int) (Math.random() * 40) + 20) - Main_runner.getDifficulty() / 2));
  }
  public void stopTimer() {
    timer.cancel();
    task.cancel();
  }
  public Hazard searchHazards(int id) {
    for (Hazard h : hazards) {
      if (h.getID() == id)
        return h;
    }
    return null;
  }

  public void reset() {
    timer.cancel();
    task.cancel();
    timer = new Timer();
    task = new TimerTask() {
      @Override
      public void run() {
        start = true;
        for (Hazard H : hazards) {
          H.stopTimer();
        }
        hazards.clear();
        Hazard.reset();
        Responder.reset();
        Main_runner.getTeam().reset();
        Commander.reset();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        timer.cancel();
        task.cancel();
      }
    };
    timer.schedule(task, 1000 * 10);
  }
  
  public String stringIncidents() {
    String output = "Incidents:";
    for (Hazard h : hazards) {
      output += "\n" + h.toString();
    }
    if (hazards.size() == 0) {
      output += "\nNo Incidents Currently.";
    }
    return output;
  }
}
