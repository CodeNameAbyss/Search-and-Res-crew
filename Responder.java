import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Responder class represents rescue team members who respond to emergencies
 * Extends Person with additional attributes like status, specialty, rank, and salary
 * Serves as parent class for specialized responder types
 */
public class Responder extends Person{
  //Instance Variables
  private String status; // Current deployment/operational status
  private String specialty; // Area of expertise/specialty
  private int rank; // Experience/skill rank level (0-15+)
  private int salary; // Annual salary amount
  private int id; // Unique identifier for this responder
  private static int count = 0; // Static counter for generating unique IDs
  private Timer timer;
  private TimerTask task;
  private static final String[] statuses = {"Ready", "Deployed", "Cooldown", "Injured", "Unconscious", "Deceased"}; // Available status types
  private static final String[] types = {"reconnaissance", "paramedic", "k-9_unit", "firefighter", "enforcer", "engineer", "cyber_analyst", "communicator", "investigator", "engineer", "emt", "hazmat", "sar", "meteorologist", "animal_control", "swat"}; // Available special types}; // Lowercase versions for comparison
  private static final String[] specialties = {"engineer", "cyber_analyst", "communicator", "investigator", "engineer", "emt", "hazmat", "sar", "meteorologist", "animal_control", "swat"};

  //Constructors
  /**
   * Default constructor - creates a responder with default values
   */
  public Responder(String specialty) {
    this((int) (Math.random() * 26) + 75, (int) (Math.random() * 26) + 75, "Ready", specialty, (int) (Math.random() * 10), 40000);
  }
  /**
   * Constructor with status and specialty
   * @param morale - Mental/emotional state rating
   * @param condition - Physical condition rating
   * @param status - Current deployment status
   * @param specialty - Area of expertise
   */
  public Responder(int morale, int condition, String status, String specialty) {
    this(morale, condition, status, specialty, (int) (Math.random() * 10) + ((int) (Math.random() * 4) == 0 ? 2 : 0), 40000);
  }
  /**
   * Full parameterized constructor for Responder
   * Calculates salary based on base salary, age, rank, and specialty
   * @param morale - Mental/emotional state rating
   * @param condition - Physical condition rating
   * @param status - Current deployment status
   * @param specialty - Area of expertise
   * @param rank - Experience/skill rank
   * @param baseSalary - Base salary amount before adjustments
   */
  public Responder(int morale, int condition, String status, String specialty, int rank, int baseSalary) {
    super(morale, condition);
    this.status = status;
    this.specialty = specialty;
    this.rank = rank;
    // Calculate salary: base + random bonus + age bonus + rank bonus + specialty bonus
    this.salary = baseSalary + (int) (Math.random() * 100) * 100 + (this.age * 100) + (rank * 4000) + (Arrays.asList().contains(this.specialty) ? 5000 : 0);
    this.id = ++count; // Assign unique ID and increment counter
  }

  //Accessors
  /**
   * Gets the current status of this responder
   * @return status string
   */
  public String getStatus() {
    return status;
  }
  /**
   * Gets the specialty of this responder
   * @return specialty string
   */
  public String getSpecialty() {
    return specialty;
  }
  /**
   * Gets the rank of this responder
   * @return rank value
   */
  public int getRank() {
    return rank;
  }
  /**
   * Gets the salary of this responder
   * @return salary amount
   */
  public int getSalary() {
    return salary;
  }
  /**
   * Gets the unique ID of this responder
   * @return ID number
   */
  public int getID() {
    return id;
  }
  public static String[] getTypes() {
    return types;
  }
  public static String[] getStatuses() {
    return statuses;
  }
  public static String[] getSpecialties() {
    return specialties;
  }
  //Mutators
  /**
   * Sets the status of this responder
   * @param status - New status value
   */
  public void setStatus(String status) {
    this.status = status;
  }
  /**
   * Sets the specialty of this responder
   * @param specialty - New specialty value
   */
  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }
  /**
   * Sets the rank of this responder
   * @param rank - New rank value
   */
  public void setRank(int rank) {
    this.rank = rank;
  }
  /**
   * Sets the salary of this responder
   * @param salary - New salary amount
   */
  public void setSalary(int salary) {
    this.salary = salary;
  }


  //Methods
  public void startTimer(int duration) {
    timer = new Timer();
    timer.schedule(task = new TimerTask() {
      public void run() {
        if (status.equals("Cooldown") || getCondition() > 25) {
          status = "Ready";
          rank += (int) (Math.random() * 2);
          if (rank > 14)
            rank = 14;
        }
        setMorale(getMorale() + (int) (Math.random() * 20));
        if (getMorale() > 100)
          setMorale(100);
        if (getCondition() < 0) {
          setCondition(0);
          status = "Unconscious";
          startTimer((int) (Math.random() * 15) + 10);
        } else if (getCondition() <= 25) {
          status = "Injured";
          startTimer((int) (Math.random() * 10) + 5);
        }
        setCondition(getCondition() + (int) (Math.random() * 35) + 5 + ((int) (Math.random() * 8) == 0 ? 50 : 0));
        if (getCondition() > 100)
          setCondition(100);
      }
    }, 1000 * duration);
  }
  public void stopTimer() {
    if (timer != null) {
      timer.cancel();
    }
    task.cancel();
  }
  /**
   * Returns a detailed string representation of this responder
   * Includes ID, person info, status, specialty, rank title, and salary
   * @return Formatted string with responder details
   */
  public String toString() {
    return "ID: " + id + " - " + super.toString() + "\n\tStatus: " + status + "\n\tSpecialty: " + Commander.capitalize(specialty) + "\n\tRank: " + (rank == 0 ? "Recruit" : rank <= 1 ? "Trainee" : rank <= 2 ? "Rookie" : rank <= 4 ? "Prime" : rank <= 6 ? "Veteran" : rank <= 8 ? "Captain" : rank <= 11 ? "Legend" : rank <= 14 ? "Master" : null) + "\n\tSalary: " + salary;
  }

  public static void reset() {
    count = 0;
    for (Responder R : Main_runner.getTeam().getResponders()) {
      R.stopTimer();
    }
  }
}
