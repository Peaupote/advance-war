package fr.main.model.units.air;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.*;
import fr.main.model.units.naval.*;

/**
 * Represents a bomber
 */
public class Bomber extends Unit implements PlaneUnit {

    public static final String NAME = "Bombardier";
    public static final int PRICE   = 22000;

    public static final String PRIMARYWEAPON_NAME = "Bombes";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Infantry.class,110);
        PRIMARYWEAPON_DAMAGES.put(Mech.class,110);
        PRIMARYWEAPON_DAMAGES.put(Recon.class,105);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,105);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,95);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,90);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,35);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,95);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,105);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,105);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,105);
        PRIMARYWEAPON_DAMAGES.put(APC.class,105);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,50);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,95);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,75);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,105);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,95);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,75);
    }

    public Bomber(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public Bomber(Player player, Point point){
        super(player,point,fuelName,60,true,MoveType.AIRY,7,2,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,true),null,NAME,PRICE);
    }

    public int getFuelTurnCost(){
        return 2;
    }
}