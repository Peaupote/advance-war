package fr.main.model.units.land;

import java.awt.Point;

import fr.main.model.Player;

import fr.main.model.terrains.Terrain;

import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;

public interface AirUnit {

	public static final Unit.MoveType airMoveType=new AirMoveType(); 

	public static class AirMoveType extends Unit.MoveType{
		public String toString(){
			return "Aérien";
		}

		public int moveCost(Terrain t){
			return 1;
		}

		public boolean canMoveTo(Terrain t){
			return true;
		}
	} 

}