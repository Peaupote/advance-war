package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

import java.io.Serializable;

public abstract class Building implements AbstractBuilding, Serializable{

  protected final Terrain terrain;
  protected final int defense;

  public Building (Terrain terrain, int defense) {
    this.terrain = terrain;
    this.defense = defense;
  }

  public int getDefense () {
    return defense;
  }

  public Terrain getTerrain () {
    return terrain;
  }

}
