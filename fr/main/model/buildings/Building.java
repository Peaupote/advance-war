package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

import java.io.Serializable;

public abstract class Building implements Serializable{

  public final Terrain terrain;
  public final int defense;

  public Building (Terrain terrain, int defense) {
    this.terrain = terrain;
    this.defense = defense;
  }

}
