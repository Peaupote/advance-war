package fr.main.model.players;

import java.util.HashSet;
import java.util.ArrayList;
import java.lang.Iterable;
import java.util.Iterator;
import java.awt.Color;

import fr.main.model.State;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.commanders.Commander;

/**
 * Class representing a player
 * (a human player for the basic class
 * and AI with an inherited class).
 */
public class Player implements java.io.Serializable, Iterable<AbstractUnit> {

    /**
	 * Add Player UID.
	 */
	private static final long serialVersionUID = -2022049310756570442L;

	/**
     * The colors of the players.
     */
    public static final Color[] colors = new Color[]{
        Color.red,
        Color.blue,
        Color.green,
        Color.yellow
    };

    /**
     * The number of players.
     */
    public static int increment_id = 0;

    public final String name;
    /**
     * The number of the player.
     */
    public final int id;
    public final Color color;

    protected Commander commander;
    protected int funds;

    /**
     * true if and only if the player has lost.
     */
    protected boolean hasLost;

    /**
     * The set of units owned by the player.
     */
    protected HashSet<AbstractUnit> units;
    /**
     * The set of buildings owned by the player.
     */
    protected HashSet<OwnableBuilding> buildings;
    
    private ArrayList<State> stats;

    public Player (String name) {
        this.name = name;
        id        = ++increment_id;
        units     = new HashSet<>();
        buildings = new HashSet<>();
        stats     = new ArrayList<>();
        color     = colors[(id - 1) % colors.length];
        commander = null;
        funds     = 0;
        hasLost   = false;
    }

    /**
     * what happens when the player looses.
     */
    public void loose(){
        for (AbstractUnit a : unitList())
            a.dies();
        units.clear();
        for (OwnableBuilding b : buildingList())
            b.setOwner(null);
        buildings.clear();
        hasLost = true;

        if (Universe.get() != null)
            Universe.get().playerLoose(this);
    }

    /**
     * @return true if and only if the player lose
     */
    public boolean hasLost(){
        return hasLost;
    }

    /**
     * @param f is the quantity of money to add to the player's funds
     */
    public void addFunds(int f){
        funds += f;
    }

    /**
     * @return the funds of the player
     */
    public int getFunds(){
        return funds;
    }

    public boolean spent(int m){
        if (m > funds) return false;
        funds -= m;
        return true;
    }

    /**
     * @param fogwar represents the map and the tiles that can be seen by this player
     * set to true all the tiles that can be seen by the player
     */
    public void renderVision(boolean[][] fogwar){
        for (AbstractUnit u : units)
            u.renderVision(fogwar);
        for (OwnableBuilding b : buildings)
            b.renderVision(fogwar);
    }

    /**
     * @param c is the new commander of the player
     * @return true if and only if the commander was changed
     * the commander can be changed only if the player didn't have one before (a player can have only one commander)
     */
    public boolean setCommander(Commander c){
        if (this.commander == null){
            this.commander = c;
            return true;
        }

        return false;
    }

    /**
     * @return the commander of the player
     */
    public Commander getCommander () {
        return commander;
    }

    /**
     * @param u is the unit to add to the player's unit list
     */
    public void add(AbstractUnit u) {
        if (u.getPlayer() == this && !units.contains(u))
            units.add(u);
    }

    /**
     * @param u is the unit to remove from the player's unit list
     */
    public void remove(AbstractUnit u){
        units.remove(u);
    }

    /**
     * @param b is the building to add to the player's building list
     */
    public void addBuilding(OwnableBuilding b){
        if (b.getOwner() == this && !buildings.contains(b))
            buildings.add(b);
    }

    /**
     * @param b is the building to remove from the player's building list
     */
    public void removeBuilding(OwnableBuilding b){
        buildings.remove(b);
    }

    /**
     * @return an iterator of units
     * Player implements Iterable<AbstractUnit>
     */
    public Iterator<AbstractUnit> iterator () {
        return units.iterator();
    }

    /**
     * @return a set containing the buildings owned by the player
     */
    public HashSet<OwnableBuilding> buildingList(){
        return new HashSet<OwnableBuilding>(buildings);
    }

    /**
     * @return a set containing the units owned by the player
     */
    public ArrayList<AbstractUnit> unitList(){
        return new ArrayList<AbstractUnit>(units);
    }

    /**
     * what happens when the turn begins.
     */
    public synchronized void turnBegins(){
        commander.turnBegins();
        for (OwnableBuilding b : buildingList())
            funds += b.getIncome();
        for (AbstractUnit u : unitList())
            u.turnBegins();
        stats.add(new State(units.size(), buildings.size(), funds));
    }

    /**
     * what happens when the turn ends.
     */
    public synchronized void turnEnds(){
        for (AbstractUnit u : unitList())
            u.turnEnds();
    }

    public State[] getStats() {
      return stats.toArray(new State[stats.size()]);
    }

    /**
     * @return the color of the player
     */
	public Color getColor() {
		return color;
	}
}
