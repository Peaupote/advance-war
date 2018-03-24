package fr.main.model.units.land;

import java.awt.Point;
import java.util.HashSet;

import fr.main.model.Player;

import fr.main.model.Universe;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.SupplyUnit;
import fr.main.model.units.MoveType;

public class APC extends Unit implements LandVehicleUnit,TransportUnit,SupplyUnit{

	public static final String NAME = "VTB";
	public static final int PRICE   = 5000;

	private final HashSet<AbstractUnit> units = new HashSet<AbstractUnit>();

	public APC(Player player, Point point){
		super(player, point, fuelName, 70, false, MoveType.TREAD, 6, 1, null, null, NAME, PRICE);
	}

	public APC(Player player, int x, int y){
		this(player, new Point(x,y));
	}

	public int getCapacity(){
		return 1;
	}

    public boolean isFull(){
        return getCapacity() < units.size();
    }

    public HashSet<AbstractUnit> getUnits(){
        return new HashSet<AbstractUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return !isFull() && !units.contains(u) && u instanceof WalkingUnit;
    }

    public boolean charge(AbstractUnit u){
        if (canCharge(u)){
            Universe.get().setUnit(u.getX(), u.getY(), null);
            u.getFuel().replenish();
            return units.add(u);
        }
        else return false;
    }

    public boolean remove(AbstractUnit u, int x, int y){
        if (units.contains(u) && Math.abs(x - getX()) + Math.abs(y - getY()) == 1 && u.canStop(x, y)){
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

    public boolean canSupply(AbstractUnit u){
        return u != null && u.getPlayer() == this.getPlayer() && u.getFuel() != null;
    }
}