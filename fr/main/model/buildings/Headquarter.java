package fr.main.model.buildings;

import fr.main.model.Player;

public class Headquarter extends Building implements OwnableBuilding {

	public static final int defense=4;
	public static final int income=1000;
	public static final String name="QG";
	public static final int maximumLife=200;

	private Player owner;
	private int life;

	public Headquarter() {
		super("QG", 4, 200);
//		this.life=this.maximumLife;
//		this.owner=null;
	}

	public String toString(){
		return name;
	}

	public int getDefense(){
		return this.defense;
	}

	public int getMaximumLife(){
		return this.maximumLife;
	}

	public int getIncome(){
		return this.income;
	}

	public Player getOwner(){
		return this.owner;
	}

	public void setOwner(Player player){
		this.owner=player;
	}

	public boolean isNeutral(){
		return getOwner()==null;
	}

	public int getLife(){
		return this.life;
	}

	public boolean removeLife(Player player, int life){ // cette méthode sera à modifier pour que le joueur soit éliminé lorsque son QG est pris
		if (this.life<=life){
			setOwner(player);
			goBackToMaximumLife();
			return true;
		}else{
			this.life-=life;
			return false;
		}
	}

	public void goBackToMaximumLife(){
		this.life=this.maximumLife;
	}


}