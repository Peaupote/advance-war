package fr.main.model.units.naval;

import java.awt.Point;
import java.util.HashSet;

import fr.main.model.players.Player;

import fr.main.model.Universe;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.HealerUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.SupplyUnit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.WalkingUnit;

/**
 * Represents a repairing boat
 */
public class BlackBoat extends Unit implements NavalUnit,HealerUnit,TransportUnit,SupplyUnit {

    /**
	 * Add BlackBoat UID
	 */
	private static final long serialVersionUID = 1962756539216419078L;
	public static final String NAME = "NavLog"; // => navire logistique
    public static final int PRICE   = 7500;

    private HashSet<AbstractUnit> units = new HashSet<AbstractUnit>();

    public BlackBoat(Player player, Point point){
        super(player, point, fuelName, 60, true, MoveType.LANDER, 7, 1, null, null, NAME, PRICE);
    }

    public BlackBoat(Player player, int x, int y){
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
        return !isFull() && u.getPlayer() == getPlayer() && !units.contains(u) && u.moveCost(getX(), getY()) != null && u instanceof WalkingUnit;
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

    public boolean canSupply(AbstractUnit u){
        return u != null && u.getPlayer() == this.getPlayer() && u.getFuel() != null;
    }
}