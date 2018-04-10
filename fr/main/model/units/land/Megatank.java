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
 * Represents a big tank
 */
public class Megatank extends Unit implements TankUnit{

    /**
	 * Add Megatank UID
	 */
	private static final long serialVersionUID = -4255598680415531691L;
	public static final String NAME = "Megatank";
    public static final int PRICE   = 28000;

    public static final String PRIMARYWEAPON_NAME = "Mega canon";
    public static final String SECONDARYWEAPON_NAME = "Mitrailleuse";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES    = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();

    private static final SecondaryWeapon SECONDARYWEAPON;

    static{
        PRIMARYWEAPON_DAMAGES.put(Recon.class,135);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,125);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,65);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,55);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,35);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,115);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,115);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,125);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,125);
        PRIMARYWEAPON_DAMAGES.put(APC.class,125);
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,30);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,15);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,15);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,15);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,40);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,40);

        SECONDARYWEAPON_DAMAGES.put(Infantry.class,125);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,115);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,65);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,10);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,17);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,65);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,75);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,55);
        SECONDARYWEAPON_DAMAGES.put(APC.class,65);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,22);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,55);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

    public Megatank(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public Megatank(Player player, Point point){
        super(player,point,fuelName,70,false,MoveType.TREAD,4,1,new PrimaryWeapon(PRIMARYWEAPON_NAME,3,PRIMARYWEAPON_DAMAGES,true),SECONDARYWEAPON,NAME,PRICE);
    }
}