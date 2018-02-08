package fr.main.model.units.air;

import java.awt.Point;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.DistanceUnit;

public class Missile extends Unit implements AirUnit{

	public Missile(Player player, Point point){
		super(player, point, 99, MoveType.Airy, 6, 2, null, null);
	}

	public Missile(Player player, int x, int y){
		this(player, new Point(x,y));
	}

	public String getName () {
		return "Missile";
	}


}
