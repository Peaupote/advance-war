package fr.main.model;

import java.util.HashSet;
import java.lang.Iterable;
import java.util.Iterator;
import java.awt.Color;

import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.commanders.Commander;

public class Player implements java.io.Serializable, Iterable<AbstractUnit> {

    public static final Color[] colors = new Color[]{
        	Color.red,
			Color.blue,
			Color.green,
//        Color.getHSBColor(270, 82, 43),
        	Color.yellow
    };

    private static int increment_id = 0;

    public final String name;
    public final int id;
    public final Color color;

    private Commander commander;
    private int funds;
    private boolean hasLost;

    private HashSet<AbstractUnit> units;
    private HashSet<OwnableBuilding> buildings;

    public Player (String name) {
        this.name = name;
        id        = ++increment_id;
        units     = new HashSet<AbstractUnit>();
        buildings = new HashSet<OwnableBuilding>();
        color     = colors[id - 1];
        commander = null;
        funds     = 0;
        hasLost   = false;
    }

    public void loose(){
        Universe u = Universe.get();
        for (AbstractUnit a : units)
            u.setUnit(a.getX(), a.getY(), null);
        units.clear();
        for (OwnableBuilding b : buildingList())
            b.setOwner(null);
        buildings.clear();
        hasLost = true;
    }

    public boolean hasLost(){
        return hasLost;
    }

    public void addFunds(int f){
        funds += f;
    }

    public int getFunds(){
        return funds;
    }

    public boolean spent(int m){
        if (m > funds) return false;
        funds -= m;
        return true;
    }

    public void renderVision(boolean[][] fogwar){
        for (AbstractUnit u : units)
            u.renderVision(fogwar);
        for (OwnableBuilding b : buildings)
            b.renderVision(fogwar);
    }

    public boolean setCommander(Commander c){
        if (this.commander == null){
            this.commander = c;
            return true;
        }

        return false;
    }

    public Commander getCommander () {
        return commander;
    }

    public void add(AbstractUnit u) {
        if (u.getPlayer() == this && !units.contains(u))
            units.add(u);
    }

    public void remove(AbstractUnit u){
        units.remove(u);
    }

    public void addBuilding(OwnableBuilding b){
        if (b.getOwner() == this && !buildings.contains(b))
            buildings.add(b);
    }

    public void removeBuilding(OwnableBuilding b){
        buildings.remove(b);
    }

    public Iterator<AbstractUnit> iterator () {
        return units.iterator();
    }

    public HashSet<OwnableBuilding> buildingList(){
        return new HashSet<OwnableBuilding>(buildings);
    }

    public HashSet<AbstractUnit> unitList(){
        return new HashSet<AbstractUnit>(units);
    }

    public synchronized void turnBegins(){
        commander.turnBegins();
        for (OwnableBuilding b : buildingList())
            funds += b.getIncome();
        for (AbstractUnit u : unitList())
            u.turnBegins();
    }

    public synchronized void turnEnds(){
        for (AbstractUnit u : unitList())
            u.turnEnds();
    }

	public Color getColor() {
		return color;
	}
}
