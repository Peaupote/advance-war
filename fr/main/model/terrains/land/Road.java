package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.MoveType;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Road extends Terrain implements LandTerrain {

  private static Road instance;
  	protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

	static{
		sunnyWeatherMovementCosts.put(MoveType.Airy,1);
		sunnyWeatherMovementCosts.put(MoveType.Infantry,2);
		sunnyWeatherMovementCosts.put(MoveType.Mech,1);
	}

  protected Road() {
    super("Route",0,0,0, sunnyWeatherMovementCosts);
  }

  public static Road get () {
    if (instance == null) instance = new Road();
    return instance;
  }

}
