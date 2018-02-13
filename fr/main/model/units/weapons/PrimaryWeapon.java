package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

/*
* Primary weapons are weapons with limited ammunitions that can be range weapons. 
*/
public class PrimaryWeapon extends Weapon{

	private int ammo;
	public final int minimumRange, maximumRange, maximumAmmo;

	public PrimaryWeapon (String name, int maximumAmmo, int minimumRange, int maximumRange, Map<Class<? extends AbstractUnit>,Integer> damages){
		super(name,damages);
		this.maximumAmmo = maximumAmmo;
		this.ammo        = maximumAmmo;
		this.maximumRange=maximumRange;
		this.minimumRange=minimumRange;
	}

	public PrimaryWeapon(String name, int maximumAmmo, Map<Class<? extends AbstractUnit>,Integer> damages){
		this(name,maximumAmmo,1,1,damages);
	}

	public int getAmmunition(){
		return ammo;
	}

	public int getMaximumAmmunition(){
		return maximumAmmo;
	}

	public void replenish(){
		this.ammo=this.maximumAmmo;
	}

	public void shoot(){
		if (ammo==0)
			throw new RuntimeException("No ammunition left ! Impossible to shoot.");
		else
			ammo--;
	}

	public boolean isInRange(int actualX, int actualY, int targetX, int targetY){
		int i=Math.abs(actualX-targetX)+Math.abs(actualY-targetY);
		return i<=getMaximumRange() && i>=getMinimumRange();
	}

	public int getMinimumRange(){
		return minimumRange;
	}

	public int getMaximumRange(){
		return maximumRange;
	}

	public final boolean isContactWeapon(){
		return getMinimumRange()==1 && getMaximumRange()==1;
	}
}
