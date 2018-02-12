package fr.main.model.units.naval;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.MoveType;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.air.*;

public class Carrier extends Unit implements NavalUnit,TransportUnit<AirUnit> {

    public static final String NAME = "Porte-avion";
    public static final int PRICE   = 30000;

    public static final String PRIMARYWEAPON_NAME = "Missiles anti-aériens";

    private ArrayList<AirUnit> units = new ArrayList<AirUnit>();

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,100);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,100);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,100);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,115);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,115);
        PRIMARYWEAPON_DAMAGES.put(BlackBomb.class,120);
    }

    public Carrier(Player player, Point point){
        super(player, point, 99, MoveType.NAVAL, 5, 4, new PrimaryWeapon(PRIMARYWEAPON_NAME,9,3,8,PRIMARYWEAPON_DAMAGES), null, NAME);
    }

    public Carrier(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public int getCapacity(){
        return 2;
    }

    public boolean isFull(){
        return getCapacity()!=units.size();
    }

    public ArrayList<AirUnit> getUnits(){
        return new ArrayList<AirUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return (u instanceof AirUnit) && !isFull();
    }

    public boolean charge(AirUnit u){
        if (!isFull()){
            u.getFuel().replenish(); 
            // the carrier replenishes the planes that land on it
            return units.add(u);
        }
        return false;
    }

    public boolean remove(AirUnit u){
        return units.remove(u);
    }
}