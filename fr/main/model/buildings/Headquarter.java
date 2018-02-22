package fr.main.model.buildings;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.LandUnit;

public class Headquarter extends OwnableBuilding implements RepairBuilding<LandUnit>{

	public static final int defense     = 4;
	public static final int income      = 1000;
	public static final String name     = "QG";
	public static final int maximumLife = 200;

	public Headquarter(Terrain t, Player p){
		super(t, defense, p, maximumLife, income, name);
	}

	public boolean canRepair(AbstractUnit u){
		return u.getPlayer()==getOwner() && (u instanceof LandUnit);
	}
}
