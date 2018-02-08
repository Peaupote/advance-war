package fr.main.model.units.naval;

import java.awt.Point;

import java.util.LinkedList;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;

public class Lander extends Unit implements NavalUnit, TransportUnit<LandUnit> {

	private final LinkedList<LandUnit> units=new LinkedList<LandUnit>();

	public Lander(Player player, Point point){
		super(player, point, 99, MoveType.Lander, 6, 2, null, null);
	}

	public Lander(Player player, int x, int y){
		this(player, new Point(x,y));
	}

	public int getCapacity(){
		return 2;
	}

	public boolean isFull(){
		return getCapacity()==units.size();
	}

	public LinkedList<LandUnit> getUnits(){
		return units;
	}

	public void charge(LandUnit u){

	}

  public String getName () {
    return "Lander";
  }


}
