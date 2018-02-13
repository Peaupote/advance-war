package fr.main.model.units.land;

import java.awt.Point;
import java.util.ArrayList;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.SupplyUnit;
import fr.main.model.units.MoveType;

public class APC extends Unit implements LandVehicleUnit,TransportUnit<WalkingUnit>,SupplyUnit<AbstractUnit>{

	public static final String NAME = "VTB";
	public static final int PRICE   = 5000;

	private final ArrayList<WalkingUnit> units=new ArrayList<WalkingUnit>();

	public APC(Player player, Point point){
		super(player, point, 70, MoveType.TREAD, 6, 1, null, null, NAME);
	}

	public APC(Player player, int x, int y){
		this(player, new Point(x,y));
	}

	public int getCapacity(){
		return 1;
	}

	public boolean isFull(){
		return units.size()==getCapacity();
	}

	public ArrayList<WalkingUnit> getUnits(){
		return new ArrayList<WalkingUnit>(units);
	}

	public boolean canCharge(AbstractUnit u){
		return (u instanceof WalkingUnit) && !isFull();
	}

	public boolean charge(WalkingUnit u){
		if (!isFull())
			return units.add(u);
		return false;
	}

	public boolean remove(WalkingUnit u){
		return units.remove(u);
	}

    public boolean canSupply(AbstractUnit u){
        return u!=null && u.getPlayer()==this.getPlayer() && u.getFuel()!=null;
    }
}