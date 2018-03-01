package fr.main.model.units.air;

import java.awt.Point;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;

public class BlackBomb extends Unit implements AirUnit{

    public static final String NAME = "Missile";
    public static final int PRICE   = 25000;

    public BlackBomb(Player player, Point point){
        super(player, point, fuelName, 45, true, MoveType.AIRY, 9, 3, null, null, NAME, PRICE);
    }

    public BlackBomb(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public int getFuelTurnCost(){
        return 2;
    }
}
