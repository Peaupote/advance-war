package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;
import fr.main.model.units.CaptureBuilding;
import fr.main.model.units.MoveType;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.air.BCopter;
import fr.main.model.units.air.TCopter;

public class Mech extends Unit implements WalkingUnit,CaptureBuilding<OwnableBuilding>{

    public static final String NAME = "Bazooka";
    public static final int PRICE   = 3000;

    public static final String PRIMARYWEAPON_NAME = "Bazooka";
    public static final String SECONDARYWEAPON_NAME = "Mitraillette";

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES    = new HashMap<Class<? extends AbstractUnit>, Integer>();
    private static final Map<Class<? extends AbstractUnit>, Integer> SECONDARYWEAPON_DAMAGES  = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Recon.class,85);
        PRIMARYWEAPON_DAMAGES.put(Tank.class,55);
        PRIMARYWEAPON_DAMAGES.put(MTank.class,15);
        PRIMARYWEAPON_DAMAGES.put(Neotank.class,15);
        PRIMARYWEAPON_DAMAGES.put(Megatank.class,5);
        PRIMARYWEAPON_DAMAGES.put(AntiAir.class,65);
        PRIMARYWEAPON_DAMAGES.put(Artillery.class,70);
        PRIMARYWEAPON_DAMAGES.put(Rockets.class,80);
        PRIMARYWEAPON_DAMAGES.put(Missiles.class,85);
        PRIMARYWEAPON_DAMAGES.put(APC.class,75);

        SECONDARYWEAPON_DAMAGES.put(Infantry.class,65);
        SECONDARYWEAPON_DAMAGES.put(Mech.class,55);
        SECONDARYWEAPON_DAMAGES.put(Recon.class,18);
        SECONDARYWEAPON_DAMAGES.put(Tank.class,6);
        SECONDARYWEAPON_DAMAGES.put(MTank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Neotank.class,1);
        SECONDARYWEAPON_DAMAGES.put(Megatank.class,1);
        SECONDARYWEAPON_DAMAGES.put(AntiAir.class,6);
        SECONDARYWEAPON_DAMAGES.put(Artillery.class,32);
        SECONDARYWEAPON_DAMAGES.put(Rockets.class,35);
        SECONDARYWEAPON_DAMAGES.put(Missiles.class,35);
        SECONDARYWEAPON_DAMAGES.put(APC.class,20);
        SECONDARYWEAPON_DAMAGES.put(BCopter.class,9);
        SECONDARYWEAPON_DAMAGES.put(TCopter.class,35);
    }

    public Mech(Player p, int x, int y){
        this(p,new Point(x,y));
    }

    public Mech(Player player, Point point){
        super(player,point,WalkingUnit.FUEL_NAME,99,false,MoveType.MECH,2,2,new PrimaryWeapon(PRIMARYWEAPON_NAME,3,PRIMARYWEAPON_DAMAGES),new SecondaryWeapon(SECONDARYWEAPON_NAME,SECONDARYWEAPON_DAMAGES),NAME,PRICE);
    }

    public boolean canCapture(OwnableBuilding b){
        return b!=null && b.getOwner()!=getPlayer();
    }

}