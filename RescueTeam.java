import java.util.ArrayList;
import java.util.Arrays;

/**
 * RescueTeam class manages a complete rescue team with responders
 * Provides functionality for team building, searching, and organization
 */
public class RescueTeam {
  //Instance Variables
  private String name; // Name of the rescue team
  private ArrayList<Responder> responders; // List of team members

  //Constructors
  /**
   * Default constructor - creates a team with default name and empty lists
   */
  public RescueTeam() {
    this("Default", new ArrayList<Responder>());
  }
  /**
   * Constructor with team name only
   * @param name - Name of the rescue team
   */
  public RescueTeam(String name) {
    this(name, new ArrayList<Responder>());
  }
  /**
   * Full parameterized constructor
   * @param name - Name of the rescue team
   * @param responders - Initial list of responders
   * @param equipment - Initial list of equipment
   * @param supplies - Initial list of supplies
   */
  public RescueTeam(String name, ArrayList<Responder> responders) {
    this.name = name;
    this.responders = responders;
  }

  //Accessors
  /**
   * Gets the name of this rescue team
   * @return team name
   */
  public String getName() {
    return name;
  }
  /**
   * Gets the list of responders on this team
   * @return ArrayList of responders
   */
  public ArrayList<Responder> getResponders() {
    return responders;
  }
  //Mutators
  /**
   * Adds a responder to the team
   * @param responder - Responder to add
   */
  public void addResponder(Responder responder) {
    this.responders.add(responder);
  }
  /**
   * Removes a responder from the team
   * @param responder - Responder to remove
   * @return The removed responder, or null if not found
   */
  public Responder removeResponder(Responder responder) {
    int index = this.responders.indexOf(responder);
    if (index != -1) {
      return this.responders.remove(index);
    }
    return null;
  }

  //Methods
  /**
   * Searches for a responder by their unique ID
   * @param id - The ID to search for
   * @return The responder with matching ID, or null if not found
   */
  public Responder searchResponders(int id) {
    for (Responder R : responders) {
      if (R.getID() == id)
        return R;
    }
    return null;
  }
  /**
   * Automatically builds a balanced team based on difficulty level
   * Higher difficulty = fewer team members
   * Starts with one of each core type, then adds random responders
   * @param difficulty - Difficulty level affecting team size (1-10)
   */
  public void autoBuild(int difficulty, boolean hire) {
    //Create a new rescue team based on difficulty level
    // Add one of each core responder type
    addResponder(new Responder("reconnaissance"));
    addResponder(new Responder("k-9_unit"));
    addResponder(new Responder("paramedic"));
    addResponder(new Responder("enforcer"));
    addResponder(new Responder("firefighter"));

    int money = 1650000 - (difficulty - 1) * 100000;
    if (hire)
      money += 500000;
    // Add random responders
    while (money > 0) {
      int selection = (int) (Math.random() * 13 + 1);
      Responder R;
      switch (selection) {
        case 1:
          R = new Responder("reconnaissance");
          break;
        case 2:
          R = new Responder("k-9_unit");
          break;
        case 3:
          R = new Responder("paramedic");
          break;
        case 4:
          R = new Responder("paramedic");
          break;
        case 5:
          R = new Responder("enforcer");
          break;
        case 6:
          R = new Responder("enforcer");
          break;
        case 7:
          R = new Responder("firefighter");
          break;
        case 8:
          R = new Responder("firefighter");
          break;
        default:
          String specialist = Responder.getSpecialties()[(int) (Math.random() * Responder.getSpecialties().length)];
          R = new Responder(specialist);
          break;
      }
      if (R.getSalary() > money)
        break;
      money -= R.getSalary();
      addResponder(R);
    }
    // Sort team by specialty/class
    sort("class");
  }

  /**
   * Creates a formatted string listing responders filtered by type
   * @param method - Filter method: "responder" (all), "specialist", or specific specialty name
   * @return Formatted string with filtered responder list
   */
  public String stringResponders(String method) {
    String output = "\t\t" + this.name + " " + Commander.capitalize(method) + "s:\n";
    
    // List all responders
    if (method.equals("responder")) {
      sort("class");
      if (responders.size() == 0)
        return output + "\nNo Responders";
      for (Responder R : responders) {
        output += R + "\n";
      }
      return output;
    }
    // List only specialists
    if (method.equals("specialist")) {
      String adding = "";
      for (Responder R : responders) {
        if (Arrays.asList(Responder.getSpecialties()).contains(R.getSpecialty().toLowerCase()))
          adding += R + "\n";
      }
      if (adding.length() == 0)
        return output + "\nNo Specialists";
      output += adding;
      return output;
    }
    //List only available responders
    if (method.equals("available")) {
      sort("status");
      String adding = "";
      for (Responder R : responders) {
        if (R.getStatus().equals("Ready"))
          adding += R + "\n";
      }
      if (adding.length() == 0)
        return output + "\nNo Responders Available";
      output += adding;
      return output;
    }
    // List responders of a specific specialty
    String adding = "";
    for (Responder R : responders) {
      if (R.getSpecialty().toLowerCase().equals(method))
        adding += R + "\n";
    }
    if (adding.length() == 0)
      return output + "\nNo " + method.substring(0, 1).toUpperCase() + method.substring(1).toLowerCase() + "s";
    output += adding;
    return output;
  }

  public void reset() {
    for (Responder R : responders) {
      R.stopTimer();
    }
    responders.clear();
    name = "";
  }


  
  /**
   * Sorts the responders list using bubble sort algorithm
   * @param method - Sort criterion: "class" (specialty), "morale", "status", or "condition" (default)
   */
  public void sort(String method) {
    //Sort the rescue team based on method
    // Bubble sort implementation
    for (int i = 0; i < responders.size(); i++) {
      for (int j = 0; j < responders.size() - 1; j++) {
        switch (method) {
          case "class":
            // Sort alphabetically by specialty
            if (responders.get(j).getSpecialty().compareTo(responders.get(j + 1).getSpecialty()) > 0) {
              Responder temp = responders.get(j);
              responders.set(j, responders.get(j + 1));
              responders.set(j + 1, temp);
            }
            break;
          case "morale":
            // Sort by morale (ascending)
            if (responders.get(j).getMorale() > responders.get(j + 1).getMorale()) {
              Responder temp = responders.get(j);
              responders.set(j, responders.get(j + 1));
              responders.set(j + 1, temp);
            }
            break;
          case "status":
            // Sort alphabetically by status
            if (responders.get(j).getStatus().compareTo(responders.get(j + 1).getStatus()) > 0) {
              Responder temp = responders.get(j);
              responders.set(j, responders.get(j + 1));
              responders.set(j + 1, temp);
            }
            break;
          default:
            // Default: sort by condition (ascending)
            if (responders.get(j).getCondition() > responders.get(j + 1).getCondition()) {
              Responder temp = responders.get(j);
              responders.set(j, responders.get(j + 1));
              responders.set(j + 1, temp);
            }
            break;
        }
      }
    }
  }
}
