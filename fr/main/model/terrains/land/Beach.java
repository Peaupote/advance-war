package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.model.terrains.Terrain;

public class Beach extends Terrain implements LandTerrain,NavalTerrain {
  
	private static Beach instance;

	protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

	static{
		sunnyWeatherMovementCosts.put(MoveType.Airy,1);
		sunnyWeatherMovementCosts.put(MoveType.Lander,1);
		sunnyWeatherMovementCosts.put(MoveType.Wheel,1);
		sunnyWeatherMovementCosts.put(MoveType.Track,1);
		sunnyWeatherMovementCosts.put(MoveType.Infantry,1);
		sunnyWeatherMovementCosts.put(MoveType.Mech,1);
	}


	protected Beach() {
		super("Plage",0,0,0,sunnyWeatherMovementCosts);
	}

	public static Beach get () {
		if (instance == null)
			instance = new Beach();
		return instance;
	}
}
