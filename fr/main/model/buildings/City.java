package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;

/**
 * Represents a city
 */
public class City extends OwnableBuilding implements RepairBuilding {

    public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Ville";
    public static final int maximumLife = 200;

    public City(Player player, Point p){
        super(player, p, defense, maximumLife, income, name);
    }

    public boolean canRepair(AbstractUnit u){
        return u.getPlayer()==getOwner() && (u instanceof LandUnit);
    }
}
