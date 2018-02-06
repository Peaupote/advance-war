package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.Unit;

public abstract class Weapon {

	public static final Damage A,B,C,D,E;

	static{
		A=new Damage('A');
		B=new Damage('B');
		C=new Damage('C');
		D=new Damage('D');
		E=new Damage('E');
	}

	private final Map<Class<? extends Unit>,Damage> damages;
	public final String name;

	public Weapon(String name, Map<Class<? extends Unit>,Damage> damages){
		this.name=name;
		this.damages=damages;
	}

	/*
	*	The damage done by a unit changes depending on the type of unit is attacking.
	* 	
	*	A damage is a character between A and E, where A is the highest damage potential and E the lowest.
	*/
	public static class Damage{
		public final char damage;
		public Damage(char d){
			char dd=(char)Math.max(d,d+'A'-'a'); // on garde une lettre majuscule
			if (dd<'A' || dd>'E')
				throw new RuntimeException("Damage are a character between A and E. "+d+" is not a possible value.");
			else
				damage=dd;
		}

		public String toString(){
			return damage+"";
		}

		public char toChar(){
			return damage;
		}

		/*
		*	@return an integer representing this damage from 1 to 5 : 'A' is 5 and 'E' is 1.
		*/
		public int toInt(){
			return 5+'A'-damage;
		}
	}

	public int getDamages(Unit unit){
		if (unit!=null && damages!=null){
			for (Map.Entry<Class<? extends Unit>, Damage> entry : damages.entrySet()){
				if (entry.getKey()!=null && entry.getKey().isInstance(unit))
					return entry.getValue().toInt();
			}
		}
		throw new RuntimeException("This weapon cannot attack this unit.");
	}

	public abstract void shoot();
	public abstract boolean isInRange(int actualX, int actualY, int targetX, int targetY);

	public String toString(){
		return name;
	}

	public boolean canAttack(Unit unit){
		if (unit!=null && damages!=null){
			for (Map.Entry<Class<? extends Unit>, Damage> entry : damages.entrySet()){
				if (entry.getKey()!=null && entry.getKey().isInstance(unit))
					return true;
			}
		}
		return false;
	}
}

