package fr.main.model.units.air;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.land.*;
import fr.main.model.units.naval.*;

/**
 * Represents a stealth (a plane that can hide itself)
 */
public class Stealth extends Unit implements PlaneUnit,HideableUnit{

    /**
	 * Add Stealth UID
	 */
	private static final long serialVersionUID = -74106295396216707L;
	public static final String NAME = "Furtif";
    public static final int PRICE   = 24000;

    public static final String PRIMARYWEAPON_NAME = "Missiles";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Infantry.class,90);
        PRIMARYWEAPON_DAMAGES.put(Mech.class,90);
        PRIMARYWEAPON_DAMAGES.put(Recon.class,85);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,75);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,70);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,60);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,15);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,50);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,75);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,85);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,85);
        PRIMARYWEAPON_DAMAGES.put(APC.class,85);
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,45);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,70);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,55);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,85);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,95);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,35);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,55);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,45);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,45);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,65);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,65);
    }

	public Stealth(Player p, int x, int y){
		this(p,new Point(x,y));
	}

	public Stealth(Player player, Point point){
		super(player,point,fuelName,60,true,MoveType.AIRY,6,4,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,true),null,NAME,PRICE);
		hidden=false;
	}

	private boolean hidden;

    public boolean hide(){
        if (getMoveQuantity()!=0){
            hidden=!hidden;
            setMoveQuantity(0);
        }
        return hidden;
    }

	public boolean hidden(){
		return hidden;
	}

    public int getFuelTurnCost(){
        return (hidden)?5:2;
    }
}