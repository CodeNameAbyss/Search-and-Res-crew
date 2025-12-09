import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;
/**
 * Hazard class represents environmental dangers during rescue operations
 * Tracks hazard type, severity level, and associated risk
 */
public class Hazard {
  //Instance Variables
  private static int count = 0;
  private static int failedHazards = 0;
  private int id;
  private int known;
  private ArrayList<Rescuee> rescuees; // List of people needing rescue
  private ArrayList<Integer> assignees;
  private Object[] peril;
  private Timer timer;
  private TimerTask task;
  private ArrayList<Object[]> factors;
  //The first element of data is the name of the hazard, the rest are the types of responders that are most effective at dealing with it, the integers are the severity and than the risk of the hazard/factor
  private static final Object[][] types = {
    {"major injury", "emt", "paramedic", "hazmat", "firefighter", "enforcer", 8, 4},
    {"fire", "firefighter", "paramedic", "communicator", "enforcer", "investigator", 9, 9},
    {"hack", "cyber_analyst", "investigator", "communicator", "enforcer", "reconnaissance", 7, 8},
    {"robbery", "swat", "enforcer", "communicator", "k-9_unit", "paramedic", 6, 7},
    {"missing person", "sar", "k-9_unit", "investigator", "reconnaissance", "enforcer", 5, 6},
    {"car theft", "swat", "enforcer", "investigator", "k-9_unit", "communicator", 3, 4},
    {"car accident", "emt", "enforcer", "paramedic", "investigator", "firefighter", 7, 5},
    {"flood", "sar", "engineer", "firefighter", "communicator", "enforcer", 8, 8},
    {"criminal sighting", "swat", "investigator", "enforcer", "k-9_unit", "communicator", 4, 6},
    {"mafia operation", "swat", "investigator", "enforcer", "cyber_analyst", "reconnaissance", 7, 8},
    {"digital scam", "cyber_analyst", "investigator", "enforcer", "communicator", "engineer", 4, 5},
    {"money scandal", "investigator", "enforcer", "cyber_analyst", "communicator", "engineer", 2, 4},
    {"radiation", "hazmat", "emt", "paramedic", "communicator", "swat", 10, 10},
    {"explosion", "firefighter", "paramedic", "emt", "swat", "engineer", 10, 9},
    {"power outage", "cyber_analyst", "investigator", "engineer", "communicator", "reconnaissance", 5, 6},
    {"terrorist attack", "swat", "investigator", "enforcer", "paramedic", "emt", 10, 10},
    {"bomb threat", "k-9_unit", "swat", "investigator", "enforcer", "communicator", 8, 9},
    {"active shooter", "swat", "paramedic", "emt", "enforcer", "communicator", 9, 9},
    {"structural collapse", "sar", "engineer", "paramedic", "firefighter", "enforcer", 9, 9},
    {"tornado", "meteorologist", "sar", "engineer", "communicator", "hazmat", 8, 8},
    {"poisoning", "hazmat", "emt", "paramedic", "investigator", "enforcer", 7, 2},
    {"drug overdose", "emt", "paramedic", "hazmat", "investigator", "k-9_unit", 6, 4},
    {"drug vendor", "k-9_unit", "swat", "enforcer", "investigator", "hazmat", 3, 5},
    {"utility failure", "engineer", "communicator", "cyber_analyst", "investigator", "reconnaissance", 4, 5},
    {"noise complaint", "communicator", "enforcer", "reconnaissance", "investigator", "paramedic", 1, 1},
    {"property damage", "enforcer", "investigator", "engineer", "communicator", "reconnaissance", 2, 3},
    {"roof collapse", "engineer", "sar", "firefighter", "paramedic", "enforcer", 7, 7},
    {"solar flare", "meteorologist", "cyber_analyst", "hazmat", "communicator", "firefighter", 9, 10},
    {"suspicious activity", "enforcer", "reconnaissance", "swat", "investigator", "k-9_unit", 3, 5},
    {"kidnapping", "swat", "k-9_unit", "enforcer", "investigator", "cyber_analyst", 9, 8},
    {"devious pranks", "communicator", "investigator", "enforcer", "cyber_analyst", "reconnaissance", 1, 1},
    {"elevator collapse", "engineer", "firefighter", "paramedic", "emt", "enforcer", 6, 5},
    {"minor injury", "emt", "paramedic", "enforcer", "communicator", "investigator", 3, 2},
    {"endangered civilian", "enforcer", "sar", "paramedic", "k-9_unit", "swat", 7, 6},
    {"raccoon invasion", "animal_control", "hazmat", "enforcer", "k-9_unit", "paramedic", 1, 2},
    {"dui", "enforcer", "investigator", "paramedic", "emt", "communicator", 4, 3},
    {"suicide attempt", "emt", "paramedic", "communicator", "cyber_analyst", "investigator", 7, 6},
    {"aggressive squirrel", "animal_control", "hazmat", "paramedic", "enforcer", "k-9_unit", 1, 1},
    {"homework", "communicator", "reconnaissance", "enforcer", "engineer", "cyber_analyst", 1, 1}, // Placeholder for simulation complexity
    {"tresspasser", "swat", "enforcer", "k-9_unit", "communicator", "paramedic", 2, 3},
    {"blackmail", "cyber_analyst", "investigator", "enforcer", "communicator", "swat", 4, 6},
    {"heart attack", "emt", "paramedic", "enforcer", "firefighter", "communicator", 9, 7},
    {"harrasement", "enforcer", "reconnaissance", "cyber_analyst", "communicator", "investigator", 3, 2},
    {"stalking", "enforcer", "reconnaissance", "cyber_analyst", "investigator", "k-9_unit", 3, 5},
    {"ufo sighting", "communicator", "investigator", "hazmat", "meteorologist", "investigator", 1, 3}, // Focus on managing the public
    {"bigfoot sighting", "animal_control", "k-9_unit", "investigator", "sar", "enforcer", 1, 2},
    {"villain mastermind", "investigator", "swat", "cyber_analyst", "communicator", "enforcer", 9, 10},
    {"structural sabotage", "engineer", "investigator", "enforcer", "paramedic", "firefighter", 8, 9},
    {"public systems compromise", "cyber_analyst", "engineer", "communicator", "enforcer", "investigator", 8, 9},
    {"raining cats and dogs", "animal_control", "meteorologist", "communicator", "firefighter", "paramedic", 3, 4},
    {"cave in", "sar", "engineer", "paramedic", "firefighter", "hazmat", 9, 8},
    {"sinkhole", "engineer", "meteorologist", "firefighter", "enforcer", "hazmat", 7, 6},
    {"earthquake damage", "meteorologist", "engineer", "paramedic", "firefighter", "sar", 9, 10},
    {"tsunami", "communicator", "hazmat", "meteorologist", "engineer", "sar", 18, 15},
    {"volcanic erruption", "hazmat", "meteorologist", "communicator", "engineer", "firefighter", 18, 15},
    {"meteor strike", "hazmat", "meteorologist", "firefighter", "communicator", "paramedic", 10, 20},
    {"comet impact", "hazmat", "meteorologist", "cyber_analyst", "engineer", "paramedic", 40, 30},
    {"black hole", "hazmat", "meteorologist", "communicator", "engineer", "cyber_analyst", 100, 100}, // Focus on public response
    {"wormhole", "hazmat", "meteorologist", "reconnaissance", "engineer", "communicator", 8, 10},
    {"time dilation", "hazmat", "meteorologist", "communicator", "engineer", "cyber_analyst", 8, 8},
    {"hostage", "swat", "communicator", "enforcer", "reconnaissance", "paramedic", 7, 7},
    {"cat in a tree", "animal_control", "firefighter", "communicator", "enforcer", "paramedic", 1, 1},
    {"missing pet", "animal_control", "k-9_unit", "sar", "communicator", "firefighter", 1, 1},
    {"animal abuse", "animal_control", "hazmat", "enforcer", "investigator", "communicator", 3, 2},
    {"roadkill", "animal_control", "hazmat", "enforcer", "investigator", "firefighter", 1, 1},
    {"invasive species", "animal_control", "hazmat", "communicator", "investigator", "engineer", 2, 3},
    {"zombie outbreak", "hazmat", "enforcer", "paramedic", "emt", "firefighter", 10, 10}
  };
  private static final Object[][] factorTypes = {
    {"extreme weather", "meteorologist", "sar", "firefighter", "hazmat", "paramedic", 4, 7},
    {"fire risk", "firefighter", "engineer", "communicator", "communicator", "hazmat", 5, 8},
    {"device compromise", "cyber_analyst", "engineer", "investigator", "communicator", "enforcer", 3, 6},
    {"armed criminals", "swat", "enforcer", "k-9_unit", "communicator", "paramedic", 6, 8},
    {"poor visibility", "meteorologist", "k-9_unit", "sar", "cyber_analyst", "reconnaissance", 2, 4},
    {"confined space", "sar", "firefighter", "engineer", "paramedic", "emt", 5, 6},
    {"remote location", "k-9_unit", "sar", "reconnaissance", "communicator", "enforcer", 2, 4},
    {"crowded area", "communicator", "enforcer", "firefighter", "reconnaissance", "engineer", 4, 6},
    {"structural instability", "engineer", "sar", "firefighter", "communicator", "paramedic", 6, 7},
    {"hazardous materials", "hazmat", "swat", "paramedic", "emt", "firefighter", 7, 9},
    {"foreign language", "communicator", "cyber_analyst", "reconnaissance", "investigator", "enforcer", 1, 3},
    {"refusal of aid", "communicator", "enforcer", "paramedic", "emt", "investigator", 2, 4},
    {"criminal surveillance", "cyber_analyst", "swat", "reconnaissance", "investigator", "communicator", 4, 7},
    {"high-value target", "enforcer", "swat", "communicator", "cyber_analyst", "reconnaissance", 5, 7},
    {"evidence tampering", "investigator", "cyber_analyst", "communicator", "k-9_unit", "enforcer", 1, 3},
    {"witness tampering", "investigator", "cyber_analyst", "communicator", "enforcer", "reconnaissance", 3, 5},
    {"no cell service", "communicator", "reconnaissance", "engineer", "k-9_unit", "sar", 2, 4},
    {"inaccessible area", "sar", "engineer", "firefighter", "reconnaissance", "k-9_unit", 4, 6},
    {"interference", "cyber_analyst", "communicator", "engineer", "enforcer", "investigator", 3, 6},
    {"utility disruption", "engineer", "cyber_analyst", "enforcer", "investigator", "reconnaissance", 3, 5},
    {"media presence", "communicator", "enforcer", "cyber_analyst", "engineer", "firefighter", 1, 3},
    {"false alarm", "communicator", "reconnaissance", "investigator", "cyber_analyst", "enforcer", 1, 1},
    {"environmental sensitivity", "animal_control", "hazmat", "meteorologist", "communicator", "engineer", 4, 3},
    {"low-light conditions", "k-9_unit", "reconnaissance", "sar", "firefighter", "cyber_analyst", 2, 3},
    {"multi-jurisdictional involvement", "communicator", "reconnaissance", "cyber_analyst", "investigator", "enforcer", 2, 5},
    {"loose exotic animal", "animal_control", "hazmat", "k-9_unit", "sar", "cyber_analyst", 4, 5},
    {"wilderness environment", "animal_control", "sar", "meteorologist", "k-9_unit", "reconnaissance", 3, 5},
    {"unsecure perimeter", "enforcer", "animal_control", "k-9_unit", "cyber_analyst", "swat", 3, 4},
    {"emotional distress", "communicator", "animal_control", "emt", "paramedic", "enforcer", 2, 1},
    {"lack of resources", "communicator", "reconnaissance", "cyber_analyst", "enforcer", "engineer", 3, 4},
    {"long-duration incident", "firefighter", "meteorologist", "cyber_analyst", "enforcer", "engineer", 4, 5},
    {"vechile failure", "engineer", "cyber_analyst", "firefighter", "k-9_unit", "reconnaissance", 4, 3},
    {"mass evacuation", "communicator", "enforcer", "firefighter", "animal_control", "engineer", 5, 6}
  };
  

  //Constructors
  /**
   * Default constructor - creates a hazard with default values
   */
  public Hazard() {
    this(types[(int) (Math.random() * types.length)], 100, 100, (int) (Math.random() * 2 + Main_runner.getDifficulty() / 3) + 1);
  }
  /**
   * Parameterized constructor for Hazard
   * @param data - Name and metadata of the hazard
   * @param severity - Severity rating of the hazard
   * @param risk - Risk level rating
   */
  public Hazard(Object[] data, int severity, int risk, int factorAmount) {
    this.id = ++count;
    this.peril = data;
    this.known = (int) (Math.random() * 2);
    this.timer = new Timer();
    this.startTimer();
    this.assignees = new ArrayList<Integer>();
    this.factors = new ArrayList<Object[]>();
    for (int i = 0; i < factorAmount; i++) {
      this.factors.add(factorTypes[(int) (Math.random() * factorTypes.length)]);
    }
    this.rescuees = new ArrayList<Rescuee>();
    for (int i = 0; i < (int) (Math.random() * 4) + 1 + ((int) (Math.random() * 6) == 0 ? (int) (Math.random() * 10) : 0); i++) {
      this.rescuees.add(new Rescuee());
    }
  }

  //Accessors
  /**
   * Gets the name of this hazard
   * @return hazard name
   */
  public static int getFailedHazards() {
    return failedHazards;
  }
  public int getID() {
    return id;
  }
  public String getName() {
    return peril[0].toString();
  }
  public int getKnown() {
    return known;
  }
  public ArrayList<Rescuee> getRescuees() {
    return rescuees;
  }
  public ArrayList<Integer> getAssignees() {
    return assignees;
  }
  public ArrayList<Object[]> getEffective() {
    ArrayList<Object[]> effective = new ArrayList<Object[]>();
    for (int i = 1; i < 6; i++) {
      Object[] add = {peril[i].toString(), 7 - i};
      effective.add(add);
    }
    for (Object[] factor : factors) {
      for (int i = 1; i < 6; i++) {
        Object[] add = {factor[i].toString(), 7 - i};
        effective.add(add);
      }
    }
    return effective;
  }
  /**
   * Gets the severity rating of this hazard
   * @param k - knowledge; 0 for overall severity, 1-5 for factor severity
   * @return severity value
   */
  public int getSeverity(int k) {
    if (k >= 6)
      k = 5;
    if (k == 0 || factors.size() == 0) {
      return (int) ((Integer) peril[6]);
    } else if (k > 0 && k < 6) {
      if (factors.size() >= k) {
        return (int) (((Integer) factors.get(k - 1)[6]) + ((Integer) this.getSeverity(k - 1)));
      } else
        return this.getSeverity(k - 1);
    }
    return -1;
  }
  /**
   * Gets the risk level of this hazard
   * @return risk value
   */
  public int getRisk(int k) {
    if (k >= 6)
      k = 5;
    if (k == 0 || factors.size() == 0) {
      return (int) ((Integer) peril[7]);
    } else if (k > 0 && k < 6) {
      if (factors.size() >= k) {
        return (int) (((Integer) factors.get(k - 1)[7]) + ((Integer) this.getRisk(k - 1)));
      } else
        return this.getRisk(k - 1);
    }
    return -1;
  }
  //Mutators
  public void setKnown(int known) {
    this.known = known;
  }
  public void addAssignee(int id) {
    this.assignees.add(id);
  }
  public void setFailedHazards(int failedHazards) {
    Hazard.failedHazards = failedHazards;
  }
  /** 
   * Sets the name of this hazard
   * @param name - New hazard name
   */
  public void setName(String name) {
    this.peril[0] = name;
  }

  //Methods
  public void startTimer() {
    timer.schedule(task = new TimerTask() {
      @Override
      public void run() {
        failedHazards++;
        //System.out.println("\n\n\t!An incident was not addressed in time!\n");
        for (int E : Main_runner.getIncidents().searchHazards(id).getAssignees()) {
          Main_runner.getTeam().searchResponders(E).setMorale(Main_runner.getTeam().searchResponders(E).getMorale() - 30);
          Main_runner.getTeam().searchResponders(E).setCondition(Main_runner.getTeam().searchResponders(E).getCondition() - 50 - (int) (Math.random() * (10 + Main_runner.getIncidents().searchHazards(id).getRisk(5) / 2)) - (int) (Main_runner.getDifficulty() * 1.5));
          
          if (Main_runner.getTeam().searchResponders(E).getCondition() < 0) {
            Main_runner.getTeam().searchResponders(E).setCondition(0);
          }

          if (Main_runner.getTeam().searchResponders(E).getCondition() == 0) {
            Main_runner.getTeam().searchResponders(E).setStatus("Unconscious");
          } else if (Main_runner.getTeam().searchResponders(E).getCondition() <= 25) {
            Main_runner.getTeam().searchResponders(E).setStatus("Injured");
          } else {
            Main_runner.getTeam().searchResponders(E).setStatus("Cooldown");
          }
          Main_runner.getTeam().searchResponders(E).startTimer(((int) (Math.random() * 20) + 25 + Main_runner.getDifficulty() / 2 - Main_runner.getTeam().searchResponders(E).getCondition() / 4) > 0 ? ((int) (Math.random() * 20) + 5 + Main_runner.getDifficulty() / 2 - Main_runner.getTeam().searchResponders(E).getCondition() / 5) : 0);
        }
        Main_runner.getIncidents().searchHazards(id).stopTimer();
        Main_runner.getIncidents().removeHazards(Main_runner.getIncidents().searchHazards(id));
        Commander.checkGameOver();
      }
    }, 1000 * ((int) (Math.random() * 50 + 70 - Main_runner.getDifficulty() * 2.5)));
  }
  public void stopTimer() {
    timer.cancel();
    task.cancel();
  }

  //Main method for checking if the assignees are effective enough to deal with the hazard
  public boolean checkAssignees() {
    int sum = 0;
    ArrayList<Object[]> effective = this.getEffective();
    for (int rID : assignees) {
      Responder R = Main_runner.getTeam().searchResponders(rID);
      sum += (R.getRank() / 1.8) + (R.getMorale() / 40) + (R.getCondition() / 30) + ((75 - R.getAge()) / 20);
      ArrayList<Integer> value = new ArrayList<Integer>();
      value.add(1);
      for (Object[] e : effective) {
        //System.out.println(e[0].toString() + " " + (int) Integer.parseInt(e[1].toString()));
        if (e[0].toString().equals(R.getSpecialty())) {
          value.add((int) (Integer.parseInt(e[1].toString()) * 2.3) + 5);
        }
      }
      sum += Collections.max(value);
    }
    //System.out.println("\n\n" + sum + "/" + (int) (this.getSeverity(5) * 2 + Main_runner.getDifficulty() + rescuees.size() * 2));
    if (sum >= (int) (this.getSeverity(5) * 2 + Main_runner.getDifficulty() + rescuees.size() * 2)) {
      int random = (int) (Math.random() * 100);
      for (int rID : assignees) {
        Main_runner.getTeam().searchResponders(rID).setMorale(Main_runner.getTeam().searchResponders(rID).getMorale() - 10);
        Main_runner.getTeam().searchResponders(rID).setCondition(Main_runner.getTeam().searchResponders(rID).getCondition() - 25 - (int) (Math.random() * (10 + Main_runner.getIncidents().searchHazards(id).getRisk(5) / 2)) - (int) (Main_runner.getDifficulty() * 1.5));
        
        if (Main_runner.getTeam().searchResponders(rID).getCondition() < 0) {
          Main_runner.getTeam().searchResponders(rID).setCondition(0);
        }

        if (Main_runner.getTeam().searchResponders(rID).getCondition() == 0) {
          Main_runner.getTeam().searchResponders(rID).setStatus("Unconscious");
        } else if (Main_runner.getTeam().searchResponders(rID).getCondition() <= 25) {
          Main_runner.getTeam().searchResponders(rID).setStatus("Injured");
        } else {
          Main_runner.getTeam().searchResponders(rID).setStatus("Cooldown");
        }
        Main_runner.getTeam().searchResponders(rID).startTimer(((int) (Math.random() * 20) + 15 + Main_runner.getDifficulty() / 2 - Main_runner.getTeam().searchResponders(rID).getCondition() / 4) > 0 ? ((int) (Math.random() * 20) + 5 + Main_runner.getDifficulty() / 2 - Main_runner.getTeam().searchResponders(rID).getCondition() / 5) : 0);
      }
      return true;
    }
    return false;
  }
  /**
   * Returns a string representation of this hazard
   * @return Formatted string with hazard details
   */
  @Override
  public String toString() {
    String output = "\nID: " + id + " - " + Commander.capitalize(peril[0].toString()) + "\n\tEstimated Severity: " + (this.getSeverity(this.getKnown()) <= 10 ? "Alpha" : this.getSeverity(this.getKnown()) <= 20 ? "Bravo" : this.getSeverity(this.getKnown()) <= 30 ? "Charlie" : this.getSeverity(this.getKnown()) <= 40 ? "Delta" : "Echo") + "\n\tAnticipated Risk: " + (this.getRisk(this.getKnown()) <= 13 ? "Low" : this.getRisk(this.getKnown()) <= 26 ? "Moderate" : this.getRisk(this.getKnown()) <= 39 ? "High" : "Extreme") + "\n\tRescuees: ";
    for (Rescuee R : rescuees) {
      output += R.getName() + " " + R.getSurname() + " (" + R.getCondition() + "), ";
    }
    if (rescuees.size() > 0)
      output = output.substring(0, output.length() - 2);
    else
      output += "None";
    output += "\n\tKnown Factors Involved: ";
    String adding = "";
    for (int i = 0; i < Math.min(this.getKnown(), factors.size()); i++) {
      if (factors.size() >= i)
        adding += factors.get(i)[0] + (i + 1 < Math.min(this.getKnown(), factors.size()) ? ", " : "");
    }
    if (this.getKnown() > 0 && factors.size() > 0) 
      output += Commander.capitalize(adding);
    else
      output += "None";
    return output;
  }

  public static void reset() {
    count = 0;
    failedHazards = 0;
  }
}
