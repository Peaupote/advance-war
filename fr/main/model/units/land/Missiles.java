package fr.main.model.units.land;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.MoveType;
import fr.main.model.units.air.*;

public class Missiles extends Unit implements LandVehicleUnit{

    public static final String NAME = "Anti-aérien";
    public static final int PRICE   = 12000;

    public static final String PRIMARYWEAPON_NAME = "Missiles";
    private static final Map<Class<? extends AbstractUnit>, Integer> PRIMARYWEAPON_DAMAGES = new HashMap<Class<? extends AbstractUnit>, Integer>();

    static{
        PRIMARYWEAPON_DAMAGES.put(Fighter.class,100);
        PRIMARYWEAPON_DAMAGES.put(Bomber.class,100);
        PRIMARYWEAPON_DAMAGES.put(Stealth.class,100);
        PRIMARYWEAPON_DAMAGES.put(BCopter.class,115);
        PRIMARYWEAPON_DAMAGES.put(TCopter.class,115);
        PRIMARYWEAPON_DAMAGES.put(BlackBomb.class,120);
    }

	public Missiles(Player player, Point point){
		super(player, point, 50, MoveType.WHEEL, 4, 5, new PrimaryWeapon(PRIMARYWEAPON_NAME,6,3,5,PRIMARYWEAPON_DAMAGES), null, NAME);
	}

	public Missiles(Player player, int x, int y){
		this(player, new Point(x,y));
	}

    public boolean canAttackAfterMove(){
        return false;
    }

}
