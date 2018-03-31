package fr.main.model.buildings;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.air.*;

import java.awt.Point;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Represent an airport
 */
public class Airport extends OwnableBuilding implements FactoryBuilding, RepairBuilding {

    public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Aéroport";
    public static final int maximumLife = 200;

    public static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

    static{
        UNIT_LIST = new HashMap<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>>();
        UNIT_LIST.put(BCopter.class,   BCopter::new);
        UNIT_LIST.put(Bomber.class,    Bomber::new);
        UNIT_LIST.put(Fighter.class,   Fighter::new);
        UNIT_LIST.put(Stealth.class,   Stealth::new);
        UNIT_LIST.put(TCopter.class,   TCopter::new);
    }

    public Airport(Player player, Point p){
        super(player, p, defense, maximumLife, income, name);
    }

    public boolean canRepair(AbstractUnit u){
        return u.getPlayer()==getOwner() && (u instanceof AirUnit);
    }

    public Set<Class<? extends AbstractUnit>> getUnitList(){
        return UNIT_LIST.keySet();
    }

    public boolean create(Class<? extends AbstractUnit> c){
        try{
            BiFunction<Player, Point, ? extends AbstractUnit> constructor = UNIT_LIST.get(c);
            if (constructor != null  && Universe.get().getUnit(getX(), getY()) == null && getOwner().spent(c.getField("PRICE").getInt(null))){
                constructor.apply(getOwner(),new Point(getX(),getY())).setMoveQuantity(0);
                return true;
            }
        }catch(Exception e){}
        return false;
    }
}