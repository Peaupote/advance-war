package fr.main.model;

import fr.main.model.terrains.Terrain;
import fr.main.view.render.terrains.land.BeachRenderer;
import fr.main.view.render.terrains.land.LowlandRenderer;
import fr.main.view.render.terrains.naval.ReefRenderer;
import fr.main.view.render.terrains.naval.SeaRenderer;

public enum TerrainEnum {
  none    (-1,  null),
  lowland (0,   LowlandRenderer.get()),
  sea     (1,   SeaRenderer.get()),
  beach   (3,   BeachRenderer.get()),
  reef    (4,   ReefRenderer.get()),
  hill    (5,   null),
  mountain(6,   null),
  river   (7,   null),
  road    (8,   null),
  wood    (9,   null);

  public final int value;
  public final Terrain terrain;

  TerrainEnum(int value, Terrain terrain) {
    this.value = value;
    this.terrain = terrain;
  }

  public static TerrainEnum getTerrainEnum(int i) {
    switch (i) {
      case 0: return lowland;
      case 1: return sea;
      case 2: return beach;
      case 3: return reef;
      case 5: return hill;
      case 6: return mountain;
      case 7: return river;
      case 8: return road;
      case 9: return wood;
      default: return none;
    }
  }
}
