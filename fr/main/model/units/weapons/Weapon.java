package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

/**
 * Represents a weapon
 */
public abstract class Weapon implements java.io.Serializable {

    /**
	 * Add Weapon UID
	 */
	private static final long serialVersionUID = -9179538407425524566L;
	public final String name;
    /**
     * Represent the damages inflicted to the other units
     */
    private final Map<Class<? extends AbstractUnit>,Integer> damages;

    /**
     * true if and only if the weapon can be used after a move (most of the time it is not possible for ranged weapons)
     */
    public final boolean canAttackAfterMove;

    public Weapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages, boolean canAttackAfterMove){
        this.name    = name;
        this.damages = damages;
        this.canAttackAfterMove = canAttackAfterMove;
    }

    /**
     * Something that happens when the weapon is used
     */
    public abstract void shoot();

    /**
     * @return true if and only if the shooter can shoot the target with this weapon (so if and only if it still has ammo, the unit is in range and can be shot).
     */
    public abstract boolean canAttack(AbstractUnit shooter, AbstractUnit target);
    public abstract boolean isInRange(AbstractUnit shooter, AbstractUnit target);

    /**
     * @param map is the map represented by booleans
     * @param u is the unit using this weapon
     * @param x the horizontal position from which fire
     * @param y the vertical position from which fire
     * Set the tiles that can be fired on (if used by the unit) to true
     */
    public abstract void renderTarget(boolean[][] map, AbstractUnit u, int x, int y);

    public void renderTarget(boolean[][] map, AbstractUnit u){
        renderTarget(map, u, u.getX(), u.getY());
    }

    public String toString(){
        return name;
    }

    /**
     * @return true if and only if this weapon can shoot this unit (whatever the distance or ammo).
     */
    public boolean canShoot(AbstractUnit u){
        if (u != null)
            for (Class<? extends AbstractUnit> c : damages.keySet())
                if (c != null && c.isInstance(u))
                    return true;
        return false;
    }

    /**
     * @return an Integer whose intValue is the damage inflicted by this weapon to the unit given as a parameter. Returns null if this weapon cannot attack the unit given in parameter.
     */
    public Integer damage(AbstractUnit u){
        if (u != null)
            for(Map.Entry<Class<? extends AbstractUnit>,Integer> e : damages.entrySet())
                if (e!=null && e.getKey()!=null && e.getKey().isInstance(u))
                    return e.getValue();
        return null;
    }
}
