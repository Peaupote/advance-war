package fr.main.model.units.naval;

import java.awt.Point;

import java.util.LinkedList;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;

import fr.main.model.units.Unit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.land.LandUnit;

public class Lander extends Unit implements NavalUnit, TransportUnit<LandUnit>{

	public static final Unit.MoveType landerMoveType=new NavalUnit.NavalMoveType(){
		public boolean canMoveTo(Terrain t){
			return (t instanceof Beach) || super.canMoveTo(t);
		}

		public String toString(){
			return "Barge";
		}
	};

	private final LinkedList<LandUnit> units=new LinkedList<LandUnit>();

	public Lander(Player player, Point point, Terrain[][] ts){
		super(player, point, 99, landerMoveType, 6, 1, null, null, ts);
	}

	public Lander(Player player, int x, int y, Terrain[][] ts){
		this(player, new Point(x,y), ts);
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


}