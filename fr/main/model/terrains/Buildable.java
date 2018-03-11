package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.Weather;
import fr.main.model.buildings.Building;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;

/*
* This class is a basic terrain on which can be built any building.
*/
public abstract class Buildable extends Terrain implements AbstractBuildable<Building> {

    public Buildable (String name, int defense, int height, Map<Weather,Map<MoveType,Integer>> sunny) {
        super(name, defense, height, sunny);
    }
}
