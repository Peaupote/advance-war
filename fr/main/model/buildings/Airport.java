package fr.main.model.buildings;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.air.*;

import java.awt.Point;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Airport extends OwnableBuilding implements FactoryBuilding<AirUnit>, RepairBuilding<AirUnit> {

    public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Aéroport";
    public static final int maximumLife = 200;

    public static final Map<Class<? extends AirUnit>,BiFunction<Player,Point,? extends AirUnit>> UNIT_LIST;

    static{
        UNIT_LIST = new HashMap<Class<? extends AirUnit>,BiFunction<Player,Point,? extends AirUnit>>();
        UNIT_LIST.put(BCopter.class,   BCopter::new);
        UNIT_LIST.put(BlackBomb.class, BlackBomb::new);
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

    public Set<Class<? extends AirUnit>> getUnitList(){
        return UNIT_LIST.keySet();
    }

    public boolean create(Class<? extends AirUnit> c){
        try{
            BiFunction<Player, Point, ? extends AirUnit> constructor = UNIT_LIST.get(c);
            if (constructor != null  && Universe.get().getUnit(getX(), getY()) == null && getOwner().spent(c.getField("PRICE").getInt(null))){
                constructor.apply(getOwner(),new Point(getX(),getY()));
                return true;
            }
        }catch(Exception e){}
        return false;
    }

}
