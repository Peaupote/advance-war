package fr.main.model.units.naval;

import java.awt.Point;

import java.util.HashSet;

import fr.main.model.players.Player;

import fr.main.model.Universe;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;

/**
 * Represents a lander (a transport boat)
 */
public class Lander extends Unit implements NavalUnit, TransportUnit {

    /**
	 * Add Lander UID
	 */
	private static final long serialVersionUID = 8827130583732930678L;
	public static final String NAME = "Barge";
    public static final int PRICE   = 12000;

    private final HashSet<AbstractUnit> units = new HashSet<AbstractUnit>();

    public Lander(Player player, Point point){
        super(player, point, fuelName, 99, true, MoveType.LANDER, 6, 2, null, null, NAME, PRICE);
    }

    public Lander(Player player, int x, int y){
        this(player, new Point(x,y));
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
        return !isFull() && u.getPlayer() == getPlayer() && !units.contains(u) && u.moveCost(getX(), getY()) != null && u instanceof LandUnit;
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
            u.setMoveQuantity(0);
            setMoveQuantity(0);
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
