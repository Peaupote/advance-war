package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.MoveType;
import fr.main.model.units.naval.*;

/**
 * Represents a missile launcher which can fire at a good range
 */
public class Rockets extends Unit implements LandVehicleUnit{

    /**
	 * Add Rockers UID
	 */
	private static final long serialVersionUID = -2180587284032566672L;
	public static final String NAME = "Lance-missile";
    public static final int PRICE   = 15000;

    public static final String PRIMARYWEAPON_NAME = "Roquettes";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

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
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,60);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,85);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,55);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,60);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,60);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,60);
    }

    public Rockets(Player player, Point point){
        super(player, point, fuelName, 50, false, MoveType.WHEEL, 5, 1, new PrimaryWeapon(PRIMARYWEAPON_NAME,6,3,5,PRIMARYWEAPON_DAMAGES,false), null, NAME, PRICE);
    }

    public Rockets(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public boolean canAttackAfterMove(){
        return false;
    }
}
