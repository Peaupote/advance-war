package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;

/*
* Primary weapons are weapons with limited ammunitions that can be range weapons. 
*/
public class PrimaryWeapon extends Weapon{

	public final int maximumAmmo;
	private int ammo;

	public final int minimumRange;
	public final int maximumRange;

	public PrimaryWeapon(String name, Map<Class<? extends AbstractUnit>,Damage> damages, int maximumAmmo, int maximumRange, int minimumRange){
		super(name,damages);
		this.maximumAmmo=maximumAmmo;
		this.ammo=maximumAmmo;
		this.maximumRange=maximumRange;
		this.minimumRange=minimumRange;
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

	public final int getMinimumRange(){
		return minimumRange;
	}

	public final int getMaximumRange(){
		return maximumRange;
	}

	public final boolean isContactWeapon(){
		return getMinimumRange()==1 && getMaximumRange()==1;
	}
}