package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.naval.*;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents an artillery
 */
public class Artillery extends Unit implements LandVehicleUnit{

    /**
	 * Add Artillery UID
	 */
	private static final long serialVersionUID = 1805421806176320005L;
	public static final String NAME = "Artillerie";
    public static final int PRICE   = 6000;

    public static final String PRIMARYWEAPON_NAME = "Canon";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Infantry.class,90);
        PRIMARYWEAPON_DAMAGES.put(Mech.class,85);
        PRIMARYWEAPON_DAMAGES.put(Recon.class,80);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,70);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,45);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,40);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,15);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,75);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,75);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,80);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,80);
        PRIMARYWEAPON_DAMAGES.put(APC.class,70);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,50);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,60);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,40);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,45);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,55);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,55);
    }

	public Artillery(Player p, int x, int y){
		this(p,new Point(x,y));
	}

	public Artillery(Player player, Point point){
		super(player,point,fuelName,50,false,MoveType.TREAD,5,1,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,2,3,PRIMARYWEAPON_DAMAGES,false),null,NAME,PRICE);
	}

    public boolean canAttackAfterMove(){
        return false;
    }
}