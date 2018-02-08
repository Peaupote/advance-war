package fr.main.model.terrains.land;

import fr.main.model.units.Unit;

public class Bridge extends Road {

  private static Bridge instance;

  protected Bridge() {
  	super();
    this.name = "pont";
  }

  public static Bridge get () {
    if (instance == null) instance = new Bridge();
    return instance;
  }
  
}
