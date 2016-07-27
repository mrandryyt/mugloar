package marko.mugloar;

public class Knight {
  private String name;
  private Integer attack;
  private Integer armor;
  private Integer agility;
  private Integer endurance;

  public String toSring() {
    return "att:" + getAttack() + " armor:"+ getArmor() + " agility:" + getAgility() + " endurance:" + getEndurance() + " name: "+ getName();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAttack() {
    return attack;
  }

  public void setAttack(Integer attack) {
    this.attack = attack;
  }

  public Integer getArmor() {
    return armor;
  }

  public void setArmor(Integer armor) {
    this.armor = armor;
  }

  public Integer getAgility() {
    return agility;
  }

  public void setAgility(Integer agility) {
    this.agility = agility;
  }

  public Integer getEndurance() {
    return endurance;
  }

  public void setEndurance(Integer endurance) {
    this.endurance = endurance;
  }
}
