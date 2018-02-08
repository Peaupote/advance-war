package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;


import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.terrains.Terrain;

public class Lowland extends Buildable implements LandTerrain {
	
	private static Lowland instance;

	protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

	static{
		sunnyWeatherMovementCosts.put(MoveType.Airy,1);
		sunnyWeatherMovementCosts.put(MoveType.Wheel,2);
		sunnyWeatherMovementCosts.put(MoveType.Track,1);
		sunnyWeatherMovementCosts.put(MoveType.Infantry,1);
		sunnyWeatherMovementCosts.put(MoveType.Mech,1);
	}

	protected Lowland() {
		super("Plaine", 1, 0, 0, sunnyWeatherMovementCosts);
	}

	public static Lowland get () {
		if (instance == null)
			instance = new Lowland();
		return instance;
	}
}
