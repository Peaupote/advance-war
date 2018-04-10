package fr.main.model.units.naval;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Represents a submarine
 */
public class Sub extends Unit implements NavalUnit, HideableUnit {

    /**
	 * Add Sub UID
	 */
	private static final long serialVersionUID = -5913149726212166566L;

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
        super(player, point, fuelName, 60, true, MoveType.NAVAL, 5, 5, new PrimaryWeapon(PRIMARYWEAPON_NAME,6,PRIMARYWEAPON_DAMAGES,true), null, NAME, PRICE);
        hidden=false;
    }

    public Sub(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public boolean hide(){
        if (getMoveQuantity()!=0){
            hidden=!hidden;
            setMoveQuantity(0);
        }
        return hidden;
    }

    public boolean hidden(){
        return hidden;
    }

    public int getFuelTurnCost(){
        return (hidden)?3:1;
    }

}