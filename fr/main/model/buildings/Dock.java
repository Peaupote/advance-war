package fr.main.model.buildings;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.naval.*;

import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;
import java.util.function.BiFunction;

public class Dock extends OwnableBuilding implements FactoryBuilding,RepairBuilding {

    public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Port";
    public static final int maximumLife = 200;

    public static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

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
