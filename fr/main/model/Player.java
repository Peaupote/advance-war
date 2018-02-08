package fr.main.model;

import java.util.LinkedList;
import java.lang.Iterable;
import java.util.Iterator;

import fr.main.model.units.Unit;

public class Player implements java.io.Serializable, Iterable<Unit> {

  public final String name;
  public final int id;
  private static int increment_id = 0;
  private LinkedList<Unit> units;

  public Player (String name) {
    this.name = name;
    id = ++increment_id;
    units = new LinkedList<>();
  } 

  public void add(Unit u) {
    units.add(u);
  }

  public Iterator<Unit> iterator () {
    return units.iterator();
  }

}

