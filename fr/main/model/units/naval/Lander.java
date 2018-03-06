package fr.main.model.units.naval;

import java.awt.Point;

import java.util.ArrayList;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;

public class Lander extends Unit implements NavalUnit, TransportUnit<LandUnit> {

    public static final String NAME = "Barge";
    public static final int PRICE   = 12000;

    private final ArrayList<LandUnit> units=new ArrayList<LandUnit>();

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
        return getCapacity()==units.size();
    }

    public ArrayList<LandUnit> getUnits(){
        return new ArrayList<LandUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return (u instanceof LandUnit) && !isFull();
    }

    public boolean charge(LandUnit u){
        return units.add(u);
    }

    public boolean remove(LandUnit u){
        return units.remove(u);
    }

    public int getFuelTurnCost(){
        return 1;        
    }

}
