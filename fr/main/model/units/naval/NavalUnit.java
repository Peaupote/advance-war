package fr.main.model.units.naval;

import java.awt.Point;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.Buildable;
import fr.main.model.terrains.naval.*;
import fr.main.model.terrains.land.Beach;

import fr.main.model.buildings.Building;
import fr.main.model.buildings.Dock;

import fr.main.model.Player;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;

public interface NavalUnit extends AbstractUnit {

	public static final Unit.MoveType navalMoveType=new NavalMoveType();

	public static class NavalMoveType extends Unit.MoveType{
		public String toString(){
			return "Naval";
		}

		public int moveCost(Terrain t){
			if (canMoveTo(t)){
				if (Reef.class.isInstance(t))
					return 2;
				else
					return 1;
			}else
				throw new RuntimeException("This unit cannot go on this terrain.");
		}

		public boolean canMoveTo(Terrain t){ // basic naval units can go on any naval terrain that is not a beach, plus they can go in docks
			if (t instanceof NavalTerrain && !(t instanceof Beach)) // equivalent to '! t instanceof LandTerrain' but it is more understandable like this
				return true;
			if (t instanceof Buildable){
				Building b=((Buildable)t).getBuilding();
				if (Dock.class.isInstance(b))
					return true;
			}
			return false;
		}
	}
}