package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.players.Player;
import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.TCopter;
import fr.main.model.units.naval.*;

/**
 * Represents a basic tank
 */
public class Tank extends Unit implements TankUnit{

    /**
	 * Add Tank UID
	 */
	private static final long serialVersionUID = -7721715961361976976L;
	public static final String NAME = "Tank";
    public static final int PRICE   = 7000;

    public static final String PRIMARYWEAPON_NAME = "Canon";
    public static final String SECONDARYWEAPON_NAME = "Mitrailleuse";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES    = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();

    private static final SecondaryWeapon SECONDARYWEAPON;

    static{
        PRIMARYWEAPON_DAMAGES.put(Recon.class,85);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,55);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,15);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,15);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,10);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,65);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,70);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,85);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,85);
        PRIMARYWEAPON_DAMAGES.put(APC.class,75);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,5);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,1);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,1);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,1);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,10);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,10);

        SECONDARYWEAPON_DAMAGES.put(Infantry.class,75);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,70);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,40);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,6);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,6);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,45);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,55);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,30);
        SECONDARYWEAPON_DAMAGES.put(APC.class,45);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,10);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,40);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

    public Tank(Player p, int x, int y){
        this(p,new Point(x,y));
    }
    
    public Tank(Player player, Point point){
        super(player,point,fuelName,70,false,MoveType.TREAD,6,3,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,true),SECONDARYWEAPON,NAME,PRICE);
    }
}