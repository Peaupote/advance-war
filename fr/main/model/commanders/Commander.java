package fr.main.model.commanders;

import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;

/*
* A commander has at most two powers, he has a power bar that charges when he takes damages and he can improve or deteriorate any characteristic of any unit as a passive or active power.
*/
public abstract class Commander{

	public final PowerBar powerBar;
	private final Power small,big;
	public final String name;
	public final Player player;

	public class PowerBar{
		public final int maxValue;
		private int value;

		public PowerBar(int max){
			this.maxValue=max;
			this.value=0;
		}

		public int getValue(){
			return value;
		}
		public void addValue(int i){
			value=Math.min(maxValue,value+i);
		}
		public void removeValue(int i){
			value=Math.max(0,value-i);
		}
	}

	public class Power{
		private boolean activated;
		public final int value;
		private final PowerAction powerAction;

		public Power(int value){
			this(value, p -> {});
		}

		public Power(int value, PowerAction pa){
			this.activated=false;
			this.value=value;
			this.powerAction=pa;
		}

		public boolean activate(){
			if (activated)
				return false;
			else{
				activated=true;
				powerAction.activate(player);
				return true;
			}
		}
	}

	public Commander(String name, Player player, Power small, Power big){
		this.name=name;
		this.player=player;
		this.small=small;
		this.big=big;
		int bar=Math.max((small==null?0:small.value),(big==null?0:big.value));
		if (bar==0)
			this.powerBar=null;
		else
			this.powerBar=new PowerBar(bar);
	}

	//TODO : créer les fonctions qui donnent les modifications du commandant
	// par exemple des fonctions prenant une unité du joueur et renvoyant sa nouvelle portée, attaque, vision, déplacement,...

	public int getAttackValue(AbstractUnit u){
		return 100;
	}

	public int getDefenseValue(AbstractUnit u){
		return 100;
	}

}