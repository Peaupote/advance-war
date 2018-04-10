package fr.main.model.units.air;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.land.*;
import fr.main.model.units.naval.*;

/**
 * Represents a bomber copter
 */
public class BCopter extends Unit implements CopterUnit {

    /**
	 * Add BCopter UID
	 */
	private static final long serialVersionUID = -2175843834675603102L;
	public static final String NAME = "Hélicoptère";
    public static final int PRICE   = 9000;

    public static final String PRIMARYWEAPON_NAME   = "Missiles";
    public static final String SECONDARYWEAPON_NAME = "Mitrailleuse";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    private static final SecondaryWeapon SECONDARYWEAPON;

    static{
        PRIMARYWEAPON_DAMAGES.put(Recon.class,55);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,55);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,25);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,20);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,10);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,25);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,65);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,65);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,65);
        PRIMARYWEAPON_DAMAGES.put(APC.class,60);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,25);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,25);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,25);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,25);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,25);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,25);

        SECONDARYWEAPON_DAMAGES.put(Infantry.class,75);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,75);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,30);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,6);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,6);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,25);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,35);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,35);
        SECONDARYWEAPON_DAMAGES.put(APC.class,20);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,65);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,95);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

    public BCopter(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public BCopter(Player player, Point point){
        super(player,point,fuelName,99,true,MoveType.AIRY,6,3,new PrimaryWeapon(PRIMARYWEAPON_NAME,6,PRIMARYWEAPON_DAMAGES,true),SECONDARYWEAPON,NAME,PRICE);
    }

    public int getFuelTurnCost(){
        return 2;
    }
}