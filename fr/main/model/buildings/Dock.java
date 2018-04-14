package fr.main.model.buildings;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.naval.Battleship;
import fr.main.model.units.naval.BlackBoat;
import fr.main.model.units.naval.Carrier;
import fr.main.model.units.naval.Cruiser;
import fr.main.model.units.naval.Lander;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.units.naval.Sub;

/**
 * Represent a dock
 */
public class Dock extends OwnableBuilding implements FactoryBuilding,RepairBuilding {

    /**
	 * Add Dock UID
	 */
	private static final long serialVersionUID = -1480350998762193555L;
	public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Port";
    public static final int maximumLife = 200;

    private static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

    static{
        UNIT_LIST = new HashMap<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>>();
        UNIT_LIST.put(Battleship.class, Battleship::new);
        UNIT_LIST.put(BlackBoat.class,  BlackBoat::new);
        UNIT_LIST.put(Carrier.class,    Carrier::new);
        UNIT_LIST.put(Cruiser.class,    Cruiser::new);
        UNIT_LIST.put(Lander.class,     Lander::new);
        UNIT_LIST.put(Sub.class,        Sub::new);
    }

    public Dock(Player player, Point p){
        super(player, p, defense, maximumLife, income, name);
    }

    public boolean canRepair(AbstractUnit u){
        return u.getPlayer()==getOwner() && (u instanceof NavalUnit);
    }

    public Set<Class<? extends AbstractUnit>> getUnitList(){
        return Dock.getUnits();
    }

    public static Set<Class<? extends AbstractUnit>> getUnits(){
        return UNIT_LIST.keySet();
    }

    public boolean create(Class<? extends AbstractUnit> c){
        try{
            BiFunction<Player, Point, ? extends AbstractUnit> constructor = UNIT_LIST.get(c);
            if (constructor != null &&
                Universe.get().getUnit(getX(), getY()) == null &&
                getOwner().spent(c.getField("PRICE").getInt(null))){
                constructor.apply(getOwner(), new Point(getX(), getY())).setMoveQuantity(0);
                return true;
            }
        } catch(Exception e) {
          e.printStackTrace();
        }
        return false;
    }
}
