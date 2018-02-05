package fr.main.model.units;

import java.awt.Point;

import fr.main.model.Player;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;

/**
 * Represents a unit on the board
 */
public class Unit implements java.io.Serializable {

	/**
	 * Life in percentage
	 */
	private int life;
	private final Point location;
	private Player player;

	private PrimaryWeapon primaryWeapon;
	private SecondaryWeapon secondaryWeapon;

	public final int vision;
	private final Fuel fuel;

	public class Fuel{
		public final String name; // l'infanterie n'a pas de 'carburant' mais des 'rations' (c'est un détail mais bon)
		public final int maximumQuantity;
		private int quantity;

		public Fuel(int maximumQuantity){
			this("Carburant",maximumQuantity);
		}

		public Fuel(String name, int maximumQuantity){
			this.name=name;
			this.maximumQuantity=maximumQuantity;
			this.quantity=maximumQuantity;
		}

		/*
		*	@return true if the unit has no more fuel.
		*/
		public boolean consume(int quantity){
			this.quantity=Math.max(0,this.quantity-quantity);
			return this.quantity==0;
		}

		public void replenish(){
			this.quantity=this.maximumQuantity;
		}
	}

	public Unit (Point location) {
		this (null, location, null, 2, null, null);
	}

	public Unit (Player player, Point location, Fuel fuel, int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon) {
		this.life=100;
		this.location=location;
		this.player=player;
		this.fuel=fuel;
		this.vision=vision;
		this.primaryWeapon=primaryWeapon;
		this.secondaryWeapon=secondaryWeapon;
	}

	public final boolean removeLife(int life){
		return setLife(this.life-life);
	}

	public final void addLife(int life){
		setLife(this.life+life);
	}

	public final boolean setLife (int life) { // renvoie vrai si l'unité est encore en vie
		this.life = Math.max(0, Math.min(100, life));
		return this.life!=0;
	}

	public final int getLife () {
		return life;
	}

	public final void setPlayer (Player p) {
		if (player == null)
			this.player = p;
	}

	public final Player getPlayer(){
		return this.player;
	}

	public final int getX () {
		return location.x;
	}

	public final int getY () {
		return location.y;
	}

	public void renderVision (boolean[][] fog) {
		if (fog==null || fog.length==0 || fog[0]==null || fog[0].length==0)
			return;
		renderVision (fog,location.x,location.y,vision);

		/* ANCIENNE FORMULE (je la garde au cas où je fasse de la merde)
		for (int i = Math.max(0, location.y - 1); i < Math.min(location.y + 2, fog.length); i++)
			for (int j = Math.max(0, location.x - 1); j < Math.min(location.x + 2, fog[i].length); j++)
				fog[i][j] = true;
		*/
	}

	private void renderVision (boolean[][] fog, int x, int y, int vision){
		fog[y][x]=true;
		if (vision!=0){
			if (x!=0)
				renderVision(fog,x-1,y,vision-1);
			if (x!=fog.length)
				renderVision(fog,x+1,y,vision-1);
			if (y!=0)
				renderVision(fog,x,y-1,vision-1);
			if (y!=fog[0].length)
				renderVision(fog,x,y+1,vision-1);
		}
	}

}