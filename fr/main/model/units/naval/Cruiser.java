package fr.main.model.units.naval;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import java.util.ArrayList;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.air.*;

public class Cruiser extends Unit implements NavalUnit, TransportUnit<CopterUnit> {

    public static final String NAME = "Destroyer";
    public static final int PRICE   = 18000;

    public static final String PRIMARYWEAPON_NAME   = "Missiles";
    public static final String SECONDARYWEAPON_NAME = "Canon antiaérien";

    private final ArrayList<CopterUnit> units=new ArrayList<CopterUnit>();

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

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
        SECONDARYWEAPON_DAMAGES.put(BlackBomb.class,120);
    }

    public Cruiser(Player player, Point point){
        super(player, point, 99, MoveType.NAVAL, 6, 3, new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES), new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES), NAME);
    }

    public Cruiser(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public int getCapacity(){
        return 2;
    }

    public boolean isFull(){
        return getCapacity()==units.size();
    }

    public ArrayList<CopterUnit> getUnits(){
        return new ArrayList<CopterUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return (u instanceof CopterUnit) && !isFull();
    }

    public boolean charge(CopterUnit u){
        if (!isFull())
            return units.add(u);
        return false;
    }

    public boolean remove(CopterUnit u){
        return units.remove(u);
    }
}