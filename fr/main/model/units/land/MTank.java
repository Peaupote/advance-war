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
 * Represents a medium tank
 */
public class MTank extends Unit implements TankUnit{

    /**
	 * Add MTank UID
	 */
	private static final long serialVersionUID = 4617061891336462635L;
	public static final String NAME = "Tank-M";
    public static final int PRICE   = 16000;

    public static final String PRIMARYWEAPON_NAME = "Canon";
    public static final String SECONDARYWEAPON_NAME = "Mitrailleuse";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES    = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();

    private static final SecondaryWeapon SECONDARYWEAPON;

    static{
        PRIMARYWEAPON_DAMAGES.put(Recon.class,105);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,85);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,55);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,45);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,25);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,105);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,105);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,105);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,105);
        PRIMARYWEAPON_DAMAGES.put(APC.class,105);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,30);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,10);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,10);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,10);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,35);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,35);

        SECONDARYWEAPON_DAMAGES.put(Infantry.class,105);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,95);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,45);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,8);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,7);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,55);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,35);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,45);
        SECONDARYWEAPON_DAMAGES.put(APC.class,12);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,45);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,55);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

	public MTank(Player p, int x, int y){
		this(p,new Point(x,y));
	}

	public MTank(Player player, Point point){
		super(player,point,fuelName,50,false,MoveType.TREAD,5,1,new PrimaryWeapon(PRIMARYWEAPON_NAME,8,PRIMARYWEAPON_DAMAGES,true),SECONDARYWEAPON,NAME,PRICE);
	}
}