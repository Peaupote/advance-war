package fr.main.model.units.land;

import java.awt.Point;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.DistanceUnit;

public class Recon extends Unit implements LandVehicleUnit{

	public Recon(Player player, Point point){
		super(player, point, 99, MoveType.Wheel, 6, 2, null, null);
	}

	public Recon(Player player, int x, int y){
		this(player, new Point(x,y));
	}

	public String getName () {
		return "Recon";
	}


}
