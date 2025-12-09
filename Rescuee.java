public class Rescuee extends Person {
  //Instance Variables
  private boolean rescued;

  //Constructors
  public Rescuee() {
    this((int) (Math.random() * 101), (int) (Math.random() * 91 + 10), false);
  }
  public Rescuee(int morale, int condition, boolean rescued) {
    super(morale, condition);
    this.rescued = rescued;
  }
  
  //Accessors
  public boolean getRescued() {
    return rescued;
  }
  //Mutators
  public void setRescued(boolean rescued) {
    this.rescued = rescued;
  }

  //Methods
  public String toString() {
    return super.toString() + "\n\tRescued: " + rescued;
  }
}
