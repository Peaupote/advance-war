package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;

/*
* Secondary weapons have infinite ammunition and are contact weapons.
*/
public class SecondaryWeapon extends Weapon{

	public SecondaryWeapon(String name, Map<Class<? extends AbstractUnit>,Damage> damages){
		super(name,damages);
	}

	public void shoot(){}

	public boolean isInRange(int actualX, int actualY, int targetX, int targetY){
		return Math.abs(actualX-targetX)+Math.abs(actualY-targetY)==1;
	}

}