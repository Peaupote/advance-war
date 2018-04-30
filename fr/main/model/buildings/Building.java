package fr.main.model.buildings;

import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.air.AirUnit;

/**
 * Class representing a 'basic' building
 */
public abstract class Building implements AbstractBuilding{

    /**
	 * Add Building UID
	 */
	private static final long serialVersionUID = 5827740138370372452L;
	protected final int defense;
    protected Point location;
    public final String name;

    public Building (Point p, int defense, String name) {
        this.defense  = defense;
        this.location = p;
        this.name     = name;
//        Universe.get().setBuilding(p.x, p.y, this);
    }

    public int getX(){
        return location.x;
    }

    public int getY(){
        return location.y;
    }

	public void setLocation(Point l) {
    	this.location = l;
	}

    public String getName () {
        return name;
    }

    public int getDefense (AbstractUnit u) {
        return u instanceof AirUnit ? 0 : defense;
    }

    /**
     * @return the terrain on which the building is located
     */
    public AbstractTerrain getTerrain () {
        return Universe.get().getTerrain(location);
    }

}
