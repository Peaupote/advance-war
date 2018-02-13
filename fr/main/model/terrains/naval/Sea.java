package fr.main.model.terrains.naval;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.MoveType;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class Sea extends Terrain implements NavalTerrain {

  private static Sea instance;
  	protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

	static{
		sunnyWeatherMovementCosts.put(MoveType.LANDER,1);
		sunnyWeatherMovementCosts.put(MoveType.NAVAL,1);
	}

  protected Sea () {
    super("Mer",1, 0, 0, sunnyWeatherMovementCosts);
  }

  public static Sea get () {
    if (instance == null) instance = new Sea();
    return instance;
  }

}
