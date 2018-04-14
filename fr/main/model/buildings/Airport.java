package fr.main.model.buildings;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.Bomber;
import fr.main.model.units.air.Fighter;
import fr.main.model.units.air.Stealth;
import fr.main.model.units.air.TCopter;

/**
 * Represent an airport
 */
public class Airport extends OwnableBuilding implements FactoryBuilding, RepairBuilding {

    /**
	 * Add Airport UID
	 */
	private static final long serialVersionUID = 4371530496637090362L;
	public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Aéroport";
    public static final int maximumLife = 200;

    private static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

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
        return Airport.getUnits();
    }

    public static Set<Class<? extends AbstractUnit>> getUnits(){
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