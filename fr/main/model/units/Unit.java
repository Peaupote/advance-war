package fr.main.model.units;

import java.awt.Point;
import java.io.Serializable;

import fr.main.model.Player;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.terrains.Terrain;
/**
 * Represents a unit on the board
 */
public abstract class Unit implements Serializable {

	/**
	 * Life in percentage
	 */
	private int life;
	private final Point location;
	private Player player;

	private PrimaryWeapon primaryWeapon;
	private SecondaryWeapon secondaryWeapon;

	protected UnitType[] unitType;

	public final int vision;
	private Fuel fuel;
	protected Terrain terrain;
	public final int moveQuantity;
	private final MoveType moveType;
	protected boolean hideable;

	public class Fuel implements java.io.Serializable{
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
		public int getQuantity() {
			return quantity;
		}
	}

	/*
	* Represents how a unit moves and whether or not it can go on a specific terrain.
	*/
	public static abstract class MoveType{
		public abstract String toString();
		public abstract int moveCost(Terrain t);
		public abstract boolean canMoveTo(Terrain t);
	}


	public Unit (Point location, Terrain[][] ts) {
		this (null, location, null, null, null,0,  2, null, null, false, ts);
	}

	public Unit (Player player, Point location, UnitType[] unitType, int maxFuel, MoveType moveType, int moveQuantity , int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, boolean hideable, Terrain[][] ts) {
		this(player, location, unitType, null, moveType, moveQuantity, vision, primaryWeapon, secondaryWeapon, hideable, ts);
		this.fuel = new Fuel(maxFuel);
	}

	public Unit (Player player, Point location, UnitType[] unitType, Fuel fuel, MoveType moveType, int moveQuantity , int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, boolean hideable, Terrain[][] ts) {
		this.life=100;
		this.location=location;
		this.player=player;
		this.fuel= fuel;
		this.moveType=moveType;
		this.moveQuantity=moveQuantity;
		this.vision=vision;
		this.primaryWeapon=primaryWeapon;
		this.secondaryWeapon=secondaryWeapon;
		this.unitType = unitType;
        move(location.x, location.y, ts);
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

	public boolean move(int x, int y, Terrain[][] ts) {
		if(y >= 0 && y < ts.length && x >= 0 && x < ts[y].length && isValidTerrain(ts[y][x])) {
			location.move(x, y);
			if (terrain != null) terrain.removeUnit();
			ts[y][x].setUnit(this);
			return true;
		}
		return false;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	private boolean isValidTerrain(Terrain t) {
		// Function to modify for the move() test.
		return !(t == null || t.hasUnit());
	}

	public void renderVision (boolean[][] fog) {
		if (fog==null || fog.length==0 || fog[0]==null || fog[0].length==0)
			return;
		renderVision (fog,location.x,location.y,vision);
	}

	private void renderVision (boolean[][] fog, int x, int y, int vision){
		fog[y][x]=true;
		if (vision!=0){
			if (y!=0)
				renderVision(fog,x,y-1,vision-1);
			if (y!=fog.length-1)
				renderVision(fog,x,y+1,vision-1);
			if (x!=0)
				renderVision(fog,x-1,y,vision-1);
			if (x!=fog[0].length-1)
				renderVision(fog,x+1,y,vision-1);
		}
	}

	public boolean isUnitType(UnitType type) {
		for(UnitType t : this.unitType) if(t == type) return true;
		return false;
	}

	@Override
	public String toString() {
		String out = "Name" +
				"\nHP : " + Integer.toString(life);
		if(player != null)
			out += "\nPlayer : " + player.name;
			out += "\nVision : " + Integer.toString(vision);
		if(fuel != null)
			out += "\nFuel : " + Integer.toString(fuel.getQuantity());
		if(primaryWeapon != null)
			out += "\nPrimary : " + primaryWeapon.name;
		if(secondaryWeapon != null)
			out += "\nSecondary : " + secondaryWeapon.name;
		return out;
	}

	protected UnitType[] addType(UnitType a, UnitType b) {
		UnitType[] ts = {a,b};
		return ts;
	}
	protected UnitType[] addType(UnitType[] as, UnitType b) {
		UnitType[] bs = {b};
		return addType(as, bs);
	}
	protected UnitType[] addType(UnitType[] as, UnitType[] bs) {
		UnitType[] c = new UnitType[as.length + bs.length];
		System.arraycopy(as, 0, c, 0, as.length);
		System.arraycopy(bs, 0, c, as.length, bs.length);

		return c;
	}
}