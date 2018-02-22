package fr.main.model.units.naval;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;

import fr.main.model.units.Unit;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;

public class Sub extends Unit implements NavalUnit, HideableUnit {

    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    public static final String PRIMARYWEAPON_NAME = "Torpilles";

    public static final String NAME = "Sous-marin";
    public static final int PRICE   = 20000;

    private boolean hidden;

    static{
        PRIMARYWEAPON_DAMAGES.put(Cruiser.class,25);
        PRIMARYWEAPON_DAMAGES.put(Sub.class,55);
        PRIMARYWEAPON_DAMAGES.put(Battleship.class,65);
        PRIMARYWEAPON_DAMAGES.put(Carrier.class,75);
        PRIMARYWEAPON_DAMAGES.put(Lander.class,95);
        PRIMARYWEAPON_DAMAGES.put(BlackBoat.class,95);
    }

    public Sub(Player player, Point point){
        super(player, point, fuelName, 60, true, MoveType.NAVAL, 5, 5, new PrimaryWeapon(PRIMARYWEAPON_NAME,6,PRIMARYWEAPON_DAMAGES), null, NAME, PRICE);
        hidden=false;
    }

    public Sub(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public boolean hide(){
        hidden=!hidden;
        return hidden;
    }

    public boolean hidden(){
        return hidden;
    }

    public int getFuelTurnCost(){
        return (hidden)?3:1;
    }

}