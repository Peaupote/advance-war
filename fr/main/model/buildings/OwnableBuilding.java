package fr.main.model.buildings;

import fr.main.model.Player;
import fr.main.model.units.Unit;

public abstract class OwnableBuilding implements AbstractOwnableBuilding{

	protected int life;
	public final int maximumLife;
	protected Player owner;
	public final int defense;
	public final int income;
	public final String name;

	public OwnableBuilding(Player owner, int maximumLife, int defense, int income, String name){
		this.maximumLife=maximumLife;
		this.life=maximumLife;
		this.owner=owner;
		this.defense=defense;
		this.income=income;
		this.name=name;
	}

	public String toString(){
		return name;
	}

	public int getDefense(){
		return this.defense;
	}

	public int getIncome(){
		return this.income;
	}

	public Player getOwner(){
		return owner;
	}

	public void setOwner(Player p){
		owner=p;
	}

	public boolean isNeutral(){
		return owner==null;
	}

	public int getMaximumLife(){
		return maximumLife;
	}

	public int getLife(){
		return life;
	}

	public boolean removeLife(Player player, int life){
		if (this.life<=life){
			setOwner(player);
			goBackToMaximumLife();
			return true;
		}else{
			this.life-=life;
			return false;
		}
	}

	public boolean removeLife(Unit u){
		return removeLife(u.getPlayer(),u.getLife());
	}

	public void goBackToMaximumLife(){
		this.life=getMaximumLife();
	}

}