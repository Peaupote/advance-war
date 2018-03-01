package fr.main.model.units.air;

import java.awt.Point;
import java.util.ArrayList;

import fr.main.model.Player;
import fr.main.model.units.MoveType;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.WalkingUnit;
import fr.main.model.units.TransportUnit;

public class TCopter extends Unit implements CopterUnit,TransportUnit<WalkingUnit> {

    public static final String NAME = "Chinook";
    public static final int PRICE   = 5000;

    private final ArrayList<WalkingUnit> units=new ArrayList<WalkingUnit>();

    public TCopter(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public TCopter(Player player, Point point){
        super(player,point,fuelName,99,true,MoveType.AIRY,6,2,null,null,NAME,PRICE);
    }

    public int getCapacity(){
        return 2;
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
        return units.add(u);
    }

    public boolean remove(WalkingUnit u){
        return units.remove(u);
    }

    public int getFuelTurnCost(){
        return 2;
    }

}