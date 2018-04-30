package fr.main.model.buildings;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.APC;
import fr.main.model.units.land.AntiAir;
import fr.main.model.units.land.Artillery;
import fr.main.model.units.land.Infantry;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.land.MTank;
import fr.main.model.units.land.Mech;
import fr.main.model.units.land.Megatank;
import fr.main.model.units.land.Missiles;
import fr.main.model.units.land.Neotank;
import fr.main.model.units.land.Recon;
import fr.main.model.units.land.Rockets;
import fr.main.model.units.land.Tank;

/**
 * Represent a barrack
 */
public class Barrack extends OwnableBuilding implements FactoryBuilding, RepairBuilding {

    /**
	 * Add Barrack UID
	 */
	private static final long serialVersionUID = -3632884447221501278L;
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

    private static final Map<Class<? extends AbstractUnit>,BiFunction<Player,Point,? extends AbstractUnit>> UNIT_LIST;

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
        return Barrack.getUnits();
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
        }catch(Exception e){e.printStackTrace();}
        return false;
    }
}
