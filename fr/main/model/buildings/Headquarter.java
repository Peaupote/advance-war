package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;

public class Headquarter extends OwnableBuilding implements RepairBuilding{

    public static final int defense     = 4;
    public static final int income      = 1000;
    public static final String name     = "QG";
    public static final int maximumLife = 200;

    public Headquarter(Player p, Point pos){
        super(p, pos, defense, maximumLife, income, name);
    }

    public boolean canRepair(AbstractUnit u){
        return u.getPlayer()==getOwner() && (u instanceof LandUnit);
    }

    public void setOwner(Player p){
        if (owner != null){
            owner.removeBuilding(this);
            for (OwnableBuilding b : owner.buildingList())
                b.setOwner(p);
            owner.loose();
        }
        new City(p, new Point (getX(),getY()));
    }
}
