package fr.main.model;

import java.util.LinkedList;
import java.lang.Iterable;
import java.util.Iterator;
import java.awt.Color;

import fr.main.model.units.Unit;
import fr.main.model.commanders.Commander;

public class Player implements java.io.Serializable, Iterable<Unit> {

    private static final Color[] colors = new Color[]{
        Color.red,
        Color.blue,
        Color.white,
        Color.yellow
    };

    private static int increment_id = 0;

    public final String name;
    public final int id;
    public final Color color;
    public Commander commander;

    private LinkedList<Unit> units;

    public Player (String name) {
        this.name = name;
        id = ++increment_id;
        units = new LinkedList<>();
        color = colors[id - 1];
        commander=null;
    }

    public boolean setCommander(Commander c){
        if (this.commander==null){
            this.commander=c;
            return true;
        }
        else
            return false;
    }

    public void add(Unit u) {
        if (u.setPlayer(this))
            units.add(u);
    }

    public Iterator<Unit> iterator () {
        return units.iterator();
    }
}