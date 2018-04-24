package fr.main.model.units.naval;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.land.*;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents a battleship (a boat with a huge range)
 */
public class Battleship extends Unit implements NavalUnit {

    /**
	 * Add Battleship UID
	 */
	private static final long serialVersionUID = -5985539079224875484L;

	private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    public static final String NAME = "Cuirassée";
    public static final int PRICE   = 28000;

    public static final String PRIMARYWEAPON_NAME = "Canon";

    static{
        PRIMARYWEAPON_DAMAGES.put(Infantry.class,95);
        PRIMARYWEAPON_DAMAGES.put(Mech.class,90);
        PRIMARYWEAPON_DAMAGES.put(Recon.class,90);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,80);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,55);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,50);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,25);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,85);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,80);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,85);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,90);
        PRIMARYWEAPON_DAMAGES.put(APC.class,80);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,95);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,95);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,50);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,60);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,95);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,95);
    }

    public Battleship(Player player, Point point){
        super(player, point, fuelName, 99, true, MoveType.NAVAL, 5, 2, new PrimaryWeapon(PRIMARYWEAPON_NAME,9,2,6,PRIMARYWEAPON_DAMAGES,false), null, NAME, PRICE);
    }

    public Battleship(Player player, int x, int y){
        this(player, new Point(x,y));
    }
    
    public boolean canAttackAfterMove(){
        return false;
    }

    public int getFuelTurnCost(){
        return 1;        
    }

}