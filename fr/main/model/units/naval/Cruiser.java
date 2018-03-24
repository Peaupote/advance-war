package fr.main.model.units.naval;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;

import fr.main.model.Universe;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.air.*;

public class Cruiser extends Unit implements NavalUnit, TransportUnit {

    public static final String NAME = "Destroyer";
    public static final int PRICE   = 18000;

    public static final String PRIMARYWEAPON_NAME   = "Missiles";
    public static final String SECONDARYWEAPON_NAME = "Canon antiaérien";

    private final HashSet<AbstractUnit> units = new HashSet<AbstractUnit>();

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    public static final SecondaryWeapon SECONDARYWEAPON;

    static{
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,25);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,90);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,5);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,5);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,25);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,25);

        SECONDARYWEAPON_DAMAGES.put(Fighter.class,85);
        SECONDARYWEAPON_DAMAGES.put(Bomber.class,100);
        SECONDARYWEAPON_DAMAGES.put(Stealth.class,100);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,105);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,105);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

    public Cruiser(Player player, Point point){
        super(player, point, fuelName, 99, true, MoveType.NAVAL, 6, 3, new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,false), SECONDARYWEAPON, NAME, PRICE);
    }

    public Cruiser(Player player, int x, int y){
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
        return !isFull() && !units.contains(u) && u instanceof CopterUnit;
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

    public int getFuelTurnCost(){
        return 1;
    }
}