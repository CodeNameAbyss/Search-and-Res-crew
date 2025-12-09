import java.util.Scanner;
import java.util.Arrays;

/**
 * Main_runner class contains the main entry point for the Rescue Simulation game
 * Handles user interaction, team setup, and the hiring system
 */
public class Main_runner {
  private static Incidents incidents;
  private static RescueTeam team;
  private static Integer difficulty;
  private static Commander commander;
  
  public static Incidents getIncidents() {
    return incidents;
  }
  public static RescueTeam getTeam() {
    return team;
  }
  public static Integer getDifficulty() {
    return difficulty;
  }
  public static Commander getCommander() {
    return commander;
  }
  
  /**
   * Main method - entry point of the application
   * Guides the user through difficulty selection, commander creation, and team building
   * @param args - Command line arguments (not used)
   */
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    Start: while (true) {
      if (Incidents.getStart()) {
        try {
          incidents = new Incidents();
          // Welcome message and difficulty selection
          System.out.print("\t\tWelcome to the Rescue Simulation!\nSelect a difficulty level from 1 to 10: ");
          Incidents.setStart(false);
          boolean invalidName = false;
          difficulty = 0;
    
          // Input validation loop for difficulty
          do {
            if (invalidName)
              System.out.print("\nInvalid input. Please try again: ");
            invalidName = true;
            try {
              difficulty = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
              continue; // Re-prompt if input isn't a number
            }
          } while (!(difficulty instanceof Integer && difficulty > 0 && difficulty < 11));
    
          // Get commander name and create commander object
          System.out.print("\nPlease enter your name: ");
          commander = new Commander(scan.nextLine().split(" "));
    
          // Welcome message with difficulty-based description
          System.out.print("\nWelcome, Commander " + (commander.getSurname().equals("") ? commander.getName() : commander.getSurname()) + "! You have been appointed to lead the local rescue team in a " + (difficulty < 3 ? "low" : difficulty < 6 ? "moderate" : difficulty < 10 ? "high" : "severe") + " level distress zone.\nName your rescue team: ");
          team = new RescueTeam(scan.nextLine());
    
          // Ask if user wants to manually select team or auto-build
          System.out.print("\nHandpick your team and resources? (y/n): ");
          if (!scan.nextLine().toLowerCase().equals("y")) {
            // Auto-build mode: generate team automatically
            team.autoBuild(difficulty, false);
          } else {
            // Manual team selection mode
            // Create a pool of available responders for hire
            RescueTeam forHire = new RescueTeam("For Hire");
            forHire.autoBuild((int) (Math.random() * 5) - 20, true); // Generate random pool
    
            // Calculate budget based on difficulty
            int money = 2000000 - (difficulty - 1) * 100000;
            boolean purchasable = true;
            String input = "";
    
            // Main hiring menu loop
            HireMenu: while (!input.toLowerCase().equals("next")) {
              System.out.print("\n\n\t\tYou have $" + money +" to hire.\n\t\tAvailable Hires -  Enter a category (\"Next\" to move on, \"Team\" to view hired applicants): ");
              boolean invalid = false;
    
              // Category selection loop
              do {
                if (invalid)
                  System.out.print("\nInvalid input. Please try again: ");
                input = scan.nextLine().toLowerCase();
    
                // Show current team if requested
                if (input.equals("team")) {
                  System.out.print("\n\n\t\t" + team.stringResponders("responder"));
                  continue HireMenu; // Return to main menu
                } else if (input.equals("next")) {
                  break HireMenu;
                }
                invalid = true;
              } while (!(Arrays.asList(Responder.getTypes()).contains(input) || Arrays.asList(Responder.getSpecialties()).contains(input) || input.equals("specialist") || input.equals("responder") || input.equals("next") || input.equals("team")));
    
              String input2 = "";
              // Individual hire selection loop
              HirePage: while (!input2.toLowerCase().equals("back")) {
                // Display available responders in selected category
                System.out.print("\n" + forHire.stringResponders(input));
                System.out.print("\nEnter an ID to hire (\"Back\" to go back): ");
                boolean invalid2 = false;
    
                // Hire selection and validation loop
                do {
                  if (!purchasable) {
                    System.out.print("\nYou cannot afford this hire with your remaining $" + money + ". Try again: "); 
                    purchasable = true;
                  } else if (invalid2)
                    System.out.print("\nInvalid input. Please try again: ");
                  invalid2 = true;
                  input2 = scan.nextLine().toLowerCase();
    
                  // Go back to category selection
                  if (input2.equals("back"))
                    break;
    
                  // Validate numeric input
                  try {
                    Integer.parseInt(input2);
                  } catch (NumberFormatException e) {
                    System.out.print("\nInvalid ID: Not a number.");
                    continue HireMenu; // Invalid input, restart
                  }
    
                  Responder R = forHire.searchResponders(Integer.parseInt(input2));
                  if (R == null) {
                    System.out.print("\nInvalid ID: Either the responder does not exist or is already hired.");
                    continue HireMenu; // Invalid ID, restart;
                  }
    
                  // Check if player can afford the responder
                  if (R != null && R.getSalary() > money) {
                    purchasable = false;
                    continue HireMenu; // Not enough money, restart
                  }
                } while (!(input2.toLowerCase().equals("back") || purchasable || forHire.getResponders().size() == 0 || forHire.searchResponders(Integer.parseInt(input2)) != null));
    
                // Process the hire
                if (input2.toLowerCase().equals("back")) {
                  continue; // Go back to category menu
                } else if (input2.toLowerCase().equals("next")) {
                  break HireMenu; // Move on to simulation
                } else {
                  // Transfer responder from hire pool to player's team
                  Responder target = forHire.searchResponders(Integer.parseInt(input2));
                  team.addResponder(forHire.removeResponder(target));
                  money -= target.getSalary(); // Deduct cost
                  continue HirePage; // Continue hiring
                }
              }
            }
          }
          // Sort final team by class
          team.sort("class");
          System.out.println("\n\n\tMay the simulation begin.\nYour team must report for duty! Emergency calls will come through and add to your list of incidents periodically. You must effectively allocate your team to address as many incidents as possible. Choose however many people you think will be required and then send them. Remember, lives are on the line.");
          incidents.addHazard();
          incidents.startTimer();
    
          boolean gameOver = false;
          Simulation: while (!gameOver) {
            System.out.print("\nActions:\n\t\t1. View Incidents\n\t\t2. View Team\nEnter a number: ");
            String action = scan.nextLine();
            if (action.equals("1")) {
              String output = incidents.stringIncidents();
              System.out.println("\n\n" + output + "\n");
              if (output.length() < 35)
                continue Simulation;
              String select = "";
              boolean invalid = false;
               do {
                if (invalid) {
                  System.out.print("\nInvalid input. Please try again: ");
                } else {
                  System.out.print("\nEnter an incident by its ID for more options (\"Back\" to go back): ");
                }
                invalid = true;
                select = scan.nextLine();
                 try {
                   Integer.parseInt(select);
                 } catch (NumberFormatException e) {
                   continue Simulation;
                 }
              } while (!(select.toLowerCase().equals("back") || incidents.searchHazards(Integer.parseInt(select)) != null));
              if (select.toLowerCase().equals("back"))
                continue Simulation;
              Allocating: while (incidents.searchHazards(Integer.parseInt(select)) != null) {
                team.sort("class");
                System.out.println("\n\n" + team.stringResponders("available") + "\n\n" + incidents.searchHazards(Integer.parseInt(select)).toString());
                boolean invalid2 = false;
                String select2 = "";
                do {
                  if (invalid2)
                    System.out.print("\nInvalid input. Please try again: ");
                  else
                    System.out.print("\nSelect a responder by ID to assign to this incident (\"Back\" to go back). Type \"Y\" when done allocating: ");
                  invalid2 = true;
                  select2 = scan.nextLine();
                  if (select2.toLowerCase().equals("back"))
                    continue Simulation;
                  if (select2.toLowerCase().equals("y") && getIncidents().searchHazards(Integer.parseInt(select)) != null) {
                    if (getIncidents().searchHazards(Integer.parseInt(select)).checkAssignees()) {
                      System.out.println("\nIncident " + Integer.parseInt(select) + " has been effectively addressed!\n");
                      Commander.setPoints(Commander.getPoints() + 1);
                      Commander.checkGameOver();
                    } else {
                      //Insufficent assignees
                      getIncidents().searchHazards(Integer.parseInt(select)).setFailedHazards(getIncidents().searchHazards(Integer.parseInt(select)).getFailedHazards() + 1);
                      int random = (int) (Math.random() * 100);
                      for (int rID : incidents.searchHazards(Integer.parseInt(select)).getAssignees()) {
                        team.searchResponders(rID).setMorale(team.searchResponders(rID).getMorale() - 10);
                        team.searchResponders(rID).setCondition(team.searchResponders(rID).getCondition() - 35 - (int) (Math.random() * (10 + incidents.searchHazards(Integer.parseInt(select)).getRisk(5) / 2)) - (int) (difficulty * 1.5));

                        if (team.searchResponders(rID).getCondition() < 0) {
                          team.searchResponders(rID).setCondition(0);
                        }

                        if (team.searchResponders(rID).getCondition() == 0) {
                          team.searchResponders(rIAD).setStatus("Unconscious");
                        } else if (team.searchResponders(rID).getCondition() <= 25) {
                          team.searchResponders(rID).setStatus("Injured");
                        } else {
                          team.searchResponders(rID).setStatus("Cooldown");
                        }
                          team.searchResponders(rID).startTimer(((int) (Math.random() * 20) + 20 + difficulty / 2 - team.searchResponders(rID).getCondition() / 4) > 0 ? ((int) (Math.random() * 20) + 5 + difficulty / 2 - team.searchResponders(rID).getCondition() / 5) : 0);
                      }
                      Commander.checkGameOver();
                      System.out.println("\n\nThe incident was not addressed effectively. The responders have been sent to cooldown.\n");
                    }
                    getIncidents().searchHazards(Integer.parseInt(select)).stopTimer();
                    getIncidents().removeHazards(getIncidents().searchHazards(Integer.parseInt(select)));
                    continue Simulation;
                  } else if (getIncidents().searchHazards(Integer.parseInt(select)) == null) {
                    System.out.println("\n\nIt's too late, this incident no longer exists---it was not addressed in time.\n");
                    continue Simulation;
                  }
                  
                   try {
                     Integer.parseInt(select2);
                   } catch (NumberFormatException e) {
                     continue Allocating;
                   }
                } while (!(!select2.toLowerCase().equals("back") || !select2.toLowerCase().equals("y") || team.searchResponders(Integer.parseInt(select2)) != null));
                if (team.searchResponders(Integer.parseInt(select2)) != null) {
                  Boolean attempt = Commander.allocate(Integer.parseInt(select2), Integer.parseInt(select));
                  if (Commander.getGameOver()) {
                    incidents.reset();
                    while (true) {
                      if (Incidents.getStart())
                        continue Start;
                    }
                  }
                  if (attempt == null)
                    System.out.println("\n\nIt's too late, this incident no longer exists---it was not addressed in time.\n");
                  else if (attempt == false)  {
                    Responder R = team.searchResponders(Integer.parseInt(select2));
                    System.out.println("\n\nResponder " + R.getName() + " " + R.getSurname() + " (ID: " + R.getID() + ") is not available.\n");
                  } else {
                    Responder R = team.searchResponders(Integer.parseInt(select2));
                    System.out.println("\n\nResponder " + R.getName() + " " + R.getSurname() + " (ID: " + R.getID() + ", Specialty: " + R.getSpecialty() + ") has been assigned to incident " + Integer.parseInt(select) + ".\n");
                  }
                }
                continue Allocating;
              }
            } else if (action.equals("2")) {
              team.sort("status");
              System.out.print("\n" + team.stringResponders("responder"));
            }
          }
        } catch (Exception e) {
          System.out.println("Error: " + e.getMessage() + "\nRebooting in 10 seconds...");
          incidents.reset();
          while (true) {
            if (Incidents.getStart())
              continue Start;
          }
        }
      }
    }
  }
}
