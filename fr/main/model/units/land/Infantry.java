package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.CaptureBuilding;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.TCopter;

public class Infantry extends Unit implements WalkingUnit,CaptureBuilding<OwnableBuilding>{

    public static final String NAME = "Infanterie";
    public static final int PRICE   = 1000;

    public static final String SECONDARYWEAPON_NAME = "Mitraillette";

    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();

    private static final SecondaryWeapon SECONDARYWEAPON;

    static{
        SECONDARYWEAPON_DAMAGES.put(Infantry.class,55);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,45);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,12);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,5);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,5);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,15);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,25);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,25);
        SECONDARYWEAPON_DAMAGES.put(APC.class,14);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,7);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,30);

        SECONDARYWEAPON = new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES);
    }

	public Infantry(Player p, int x, int y){
		this(p,new Point(x,y));
	}

	public Infantry(Player player, Point point){
		super(player,point,WalkingUnit.FUEL_NAME,99,false,MoveType.INFANTRY,3,2,null,SECONDARYWEAPON,NAME,PRICE);
	}

    public boolean canCapture(OwnableBuilding b){
        return b!=null && b.getOwner()!=getPlayer();
    }
}