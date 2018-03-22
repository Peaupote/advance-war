package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.AbstractBuildable;

import java.io.Serializable;

public abstract class Building implements AbstractBuilding{

    protected final int defense;
    protected final Point location;
    public final String name;

    public Building (Point p, int defense, String name) {
        this.defense  = defense;
        this.location = p;
        this.name     = name;
        Universe.get().setBuilding(p.x, p.y, this);
    }

    public int getX(){
        return location.x;
    }

    public int getY(){
        return location.y;
    }

    public String getName () {
        return name;
    }

    public int getDefense (AbstractUnit u) {
        return u instanceof AirUnit ? 0 : defense;
    }

    public AbstractTerrain getTerrain () {
        return Universe.get().getTerrain(location);
    }

}
