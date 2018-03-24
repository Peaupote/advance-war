package fr.main.model.buildings;

import java.awt.Point;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.*;

public class Barrack extends OwnableBuilding implements FactoryBuilding, RepairBuilding {

    public static final int defense     = 3;
    public static final int income      = 1000;
    public static final String name     = "Caserne";
    public static final int maximumLife = 200;

    public Barrack(Player player, Point p){
        super(player, p, defense, maximumLife, income, name);
    }

    public boolean canRepair(AbstractUnit u){
        return u.getPlayer()==getOwner() && (u instanceof LandUnit);
    }

    public static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

    static{
        UNIT_LIST = new HashMap<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>>();
        UNIT_LIST.put(AntiAir.class,   AntiAir::new);
        UNIT_LIST.put(APC.class,       APC::new);
        UNIT_LIST.put(Artillery.class, Artillery::new);
        UNIT_LIST.put(Infantry.class,  Infantry::new);
        UNIT_LIST.put(Mech.class,      Mech::new);
        UNIT_LIST.put(Megatank.class,  Megatank::new);
        UNIT_LIST.put(Missiles.class,  Missiles::new);
        UNIT_LIST.put(MTank.class,     MTank::new);
        UNIT_LIST.put(Neotank.class,   Neotank::new);
        UNIT_LIST.put(Recon.class,     Recon::new);
        UNIT_LIST.put(Rockets.class,   Rockets::new);
        UNIT_LIST.put(Tank.class,      Tank::new);
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
