package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

public abstract class Weapon {

    public final String name;
    private final Map<Class<? extends AbstractUnit>,Integer> damages;

    public Weapon(String name, Map<Class<? extends AbstractUnit>,Integer> damages){
        this.name    = name;
        this.damages = damages;
    }

    public abstract void shoot();
    public abstract boolean isInRange(int actualX, int actualY, int targetX, int targetY);

    public String toString(){
        return name;
    }

    public boolean canShoot(AbstractUnit u){
        if (u==null)
            return false;
        for (Class<? extends AbstractUnit> c : damages.keySet())
            if (c!=null && c.isInstance(u))
                return true;
        return false;
    }

    /*
    * @returns an Integer whose intValue is the damage inflicted by this weapon to the unit given as a parameter. Returns null if this weapon cannot attack the unit given in parameter.
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