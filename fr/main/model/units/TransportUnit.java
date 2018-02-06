package fr.main.model.units;

import java.awt.*;
import java.util.LinkedList;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.Unit;
import fr.main.model.units.weapons.PrimaryWeapon;

public class TransportUnit<T extends Unit> extends Unit {

	public final int capacity;
	protected LinkedList<T> units;

	public TransportUnit(Player player, Point location, UnitType[] unitType,
						 Unit.Fuel fuel, MoveType moveType, int moveQuantity ,
						 int vision,
						 PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon,
						 boolean hideable, Terrain[][] ts, int capacity) {
		super(player, location, unitType, fuel, moveType, moveQuantity, vision, primaryWeapon, secondaryWeapon, hideable, ts);
		this.capacity = capacity;
		this.units = new LinkedList<>();
		addType(this.unitType, UnitType.TRANSPORT);
	}
	public TransportUnit(Player player, Point location, UnitType[] unitType,
				  int maxFuel, MoveType moveType, int moveQuantity ,
				  int vision,
				  PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon,
				  boolean hideable, Terrain[][] ts, int capacity) {
		super(player, location, unitType, maxFuel, moveType, moveQuantity, vision, primaryWeapon, secondaryWeapon, hideable, ts);
		this.capacity = capacity;
		this.units = new LinkedList<>();
		addType(this.unitType, UnitType.TRANSPORT);
	}

	public boolean isFull() {
		return units.size() >= capacity;
	}
	public LinkedList<T> getUnits() {
		return units;
	}
	public void charge(T unit) {
		units.add(unit);
	}

}

