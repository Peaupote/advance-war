package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.air.AirUnit;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * represents a hill
 */
public class Hill extends Terrain implements LandTerrain {

    /**
	 * Add Hill UID
	 */
	private static final long serialVersionUID = 4031715748156091572L;
	private static Hill instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();
    public static final String name="Colline";

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,3);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,2);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> rainyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        rainyWeatherMovementCosts.put(MoveType.AIRY,1);
        rainyWeatherMovementCosts.put(MoveType.WHEEL,3);
        rainyWeatherMovementCosts.put(MoveType.TREAD,2);
        rainyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        rainyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.WHEEL,4);
        snowyWeatherMovementCosts.put(MoveType.TREAD,3);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,rainyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Hill() {
        super(name, 2, 1, weatherMovementCosts);
    }

    public static Hill get () {
        if (instance == null)
            instance = new Hill();
        return instance;
    }

    @Override
    public int getBonusVision(AbstractUnit u){
        return u instanceof AirUnit ? 0 : 1;
    }

    @Override
    public int getBonusRange(AbstractUnit u, PrimaryWeapon p){
        return u instanceof AirUnit ? 0 : 1;
    }
}
