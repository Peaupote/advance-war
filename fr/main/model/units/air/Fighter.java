package fr.main.model.units.air;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents a fighter
 */
public class Fighter extends Unit implements PlaneUnit{

    /**
	 * Add Fighter UID
	 */
	private static final long serialVersionUID = 2457067878395974135L;
	public static final String NAME = "Chasseur";
    public static final int PRICE   = 20000;

    public static final String PRIMARYWEAPON_NAME = "Missiles";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,55);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,100);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,85);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,120);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,120);
    }

    public Fighter(Player p, int x, int y){
        this(p,new Point(x,y));
    }
    
    public Fighter(Player player, Point point){
        super(player,point,fuelName,99,true,MoveType.AIRY,9,2,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,true),null,NAME,PRICE);
    }

    public int getFuelTurnCost(){
        return 2;
    }
}