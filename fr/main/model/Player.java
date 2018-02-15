package fr.main.model;

import java.util.LinkedList;
import java.lang.Iterable;
import java.util.Iterator;
import java.awt.Color;

import fr.main.model.units.Unit;

public class Player implements java.io.Serializable, Iterable<Unit> {

  private static final Color[] colors = new Color[]{
    Color.red,
    Color.blue,
    Color.white,
    Color.yellow
  };

  public final String name;
  public final int id;
  public final Color color;
  private static int increment_id = 0;
  private LinkedList<Unit> units;

  public Player (String name) {
    this.name = name;
    id = ++increment_id;
    units = new LinkedList<>();
    color = colors[id - 1];
  } 

  public void add(Unit u) {
    if (u.setPlayer(this))
      units.add(u);
  }

  public Iterator<Unit> iterator () {
    return units.iterator();
  }

}

