package fr.main.model.units.land;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;
import fr.main.model.units.UnitType;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;

import java.awt.*;

public class LandUnit extends Unit{
    public LandUnit(Player player, Point location, UnitType[] unitType, Fuel fuel, MoveType moveType, int moveQuantity , int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, boolean hideable, Terrain[][] ts) {
        super(player, location, unitType, fuel, moveType, moveQuantity, vision, primaryWeapon, secondaryWeapon, hideable, ts);
    }
}
