package fr.main.model.units.naval;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.Unit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.Bomber;
import fr.main.model.units.air.Fighter;
import fr.main.model.units.air.Stealth;
import fr.main.model.units.air.TCopter;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents a carrier
 */
public class Carrier extends Unit implements NavalUnit,TransportUnit {

    /**
	 * Add Carrier UID
	 */
	private static final long serialVersionUID = -1332871683462333912L;
	public static final String NAME = "Porte-avion";
    public static final int PRICE   = 30000;

    public static final String PRIMARYWEAPON_NAME = "Missiles anti-aériens";

    private HashSet<AbstractUnit> units = new HashSet<AbstractUnit>();

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,100);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,100);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,100);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,115);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,115);
    }

    public Carrier(Player player, Point point){
        super(player, point, fuelName, 99, true, MoveType.NAVAL, 5, 4, new PrimaryWeapon(PRIMARYWEAPON_NAME,9,3,8,PRIMARYWEAPON_DAMAGES,false), null, NAME, PRICE);
    }

    public Carrier(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public boolean canAttackAfterMove(){
        return false;
    }

    public int getCapacity(){
        return 2;
    }

    public boolean isFull(){
        return getCapacity() < units.size();
    }

    public HashSet<AbstractUnit> getUnits(){
        return new HashSet<AbstractUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return !isFull() && u.getPlayer() == getPlayer() && !units.contains(u) && u.moveCost(getX(), getY()) != null && u instanceof AirUnit;
    }

    public boolean charge(AbstractUnit u){
        if (canCharge(u)){
            Universe.get().setUnit(u.getX(), u.getY(), null);
            u.getFuel().replenish();
            return units.add(u);
        }
        else return false;
    }

    public boolean canRemove(AbstractUnit u, int x, int y){
        return units.contains(u) && Math.abs(x - getX()) + Math.abs(y - getY()) == 1 && u.moveCost(getX(), getY()) != null && u.canStop(x, y);
    }

    public boolean remove(AbstractUnit u, int x, int y){
        if (canRemove(u, x, y)){
            Universe.get().setUnit(x, y, u);
            u.setLocation(x,y);
            setMoveQuantity(0);
            u.setMoveQuantity(0);
            return units.remove(u);
        }else return false;
    }

    public void turnBegins(){
        super.turnBegins();
        for (AbstractUnit u : units)
            u.getFuel().replenish();
    }

    public int getFuelTurnCost(){
        return 1;        
    }
}