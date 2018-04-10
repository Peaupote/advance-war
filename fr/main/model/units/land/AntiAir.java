package fr.main.model.units.land;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.Unit;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.Bomber;
import fr.main.model.units.air.Fighter;
import fr.main.model.units.air.Stealth;
import fr.main.model.units.air.TCopter;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents an anti air unit
 */
public class AntiAir extends Unit implements LandVehicleUnit{

    /**
	 * Add AntiAir UID
	 */
	private static final long serialVersionUID = -294124235474859813L;
	public static final String NAME = "DCA";
    public static final int PRICE   = 8000;

    public static final String PRIMARYWEAPON_NAME = "Vulcain";
    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Infantry.class,105);
        PRIMARYWEAPON_DAMAGES.put(Mech.class,105);
        PRIMARYWEAPON_DAMAGES.put(Recon.class,60);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,25);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,10);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,5);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,1);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,45);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,50);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,55);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,55);
        PRIMARYWEAPON_DAMAGES.put(APC.class,50);
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,65);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,75);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,75);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,105);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,105);
    }

    public AntiAir(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public AntiAir(Player player, Point point){
        super(player,point,fuelName,60,false,MoveType.TREAD,6,2,new PrimaryWeapon(PRIMARYWEAPON_NAME,9,PRIMARYWEAPON_DAMAGES,true),null,NAME,PRICE);
    }
}