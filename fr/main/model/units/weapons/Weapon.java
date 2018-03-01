package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

public abstract class Weapon implements java.io.Serializable {

    public final String name;
    private final Map<Class<? extends AbstractUnit>,Integer> damages;
    public final boolean canAttackAfterMove;

    public Weapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages, boolean canAttackAfterMove){
        this.name    = name;
        this.damages = damages;
        this.canAttackAfterMove = canAttackAfterMove;
    }

    public abstract void shoot();

    /*
    * @return true if and only if the shooter can shoot the target with this weapon (so if and only if it still has ammo, the unit is in range and can be shot).
    */
    public abstract boolean canAttack(AbstractUnit shooter, AbstractUnit target);
    public abstract boolean isInRange(int actualX, int actualY, int targetX, int targetY);
    public abstract void renderTarget(boolean[][] map, int x, int y, boolean enabled, boolean fullMove);

    public String toString(){
        return name;
    }

    /*
    * @return true if and only if this weapon can shoot this unit (whatever the distance or ammo).
    */
    public boolean canShoot(AbstractUnit u){
        if (u==null)
            return false;
        for (Class<? extends AbstractUnit> c : damages.keySet())
            if (c!=null && c.isInstance(u))
                return true;
        return false;
    }

    /*
    * @return an Integer whose intValue is the damage inflicted by this weapon to the unit given as a parameter. Returns null if this weapon cannot attack the unit given in parameter.
    */
    public Integer damage(AbstractUnit u){
        if (u==null)
            return null;
        for(Map.Entry<Class<? extends AbstractUnit>,Integer> e : damages.entrySet())
            if (e!=null && e.getKey()!=null && e.getKey().isInstance(u))
                return e.getValue();
        return null;
    }
}
