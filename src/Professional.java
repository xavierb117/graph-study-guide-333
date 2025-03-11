import java.util.Set;

/**
 * Represents a professional with a LinkedIn-style network of connections.
 */
class Professional {
  private String name;
  private String company;
  private int yearsOfExperience;
  private Set<Professional> connections;

  /**
   * Constructs a Professional with a name, company, experience, and connections.
   *
   * @param name              The name of the professional.
   * @param company           Their current compnay.
   * @param yearsOfExperience How many years they have worked.
   * @param connections       A set of professionals they are directly connected to.
   */
  public Professional(String name, String company, int yearsOfExperience, Set<Professional> connections) {
    this.name = name;
    this.company = company;
    this.yearsOfExperience = yearsOfExperience;
    this.connections = connections;
  }

  public String getName() {
    return name;
  }

  public Set<Professional> getConnections() {
    return connections;
  }

  public String getCompany() {
    return company;
  }

  public int getYearsOfExperience() {
    return yearsOfExperience;
  }
}
