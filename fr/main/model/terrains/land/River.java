package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.MoveType;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class River extends Terrain implements LandTerrain {

  private static River instance;
	protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

	static{
		sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
		sunnyWeatherMovementCosts.put(MoveType.INFANTRY,2);
		sunnyWeatherMovementCosts.put(MoveType.MECH,1);
	}

  protected River() {
    super("Rivière",0,0,0, sunnyWeatherMovementCosts);
  }

  public static River get () {
    if (instance == null) instance = new River();
    return instance;
  }

}
