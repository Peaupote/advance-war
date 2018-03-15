package fr.main.model;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;

import java.time.temporal.TemporalAccessor;

public enum TerrainEnum {
  none     (-1, null),
  lowland  (0,  Lowland.get()),
  sea      (1,  Sea.get()),
  beach    (3,  Beach.get()),
  reef     (4,  Reef.get()),
  hill     (5,  Hill.get()),
  mountain (6,  Mountain.get()),
  river    (7,  River.get()),
  road     (8,  Road.get()),
  wood     (9,  Wood.get()),
  bridge   (10, Bridge.get());

  public final int value;
  public final AbstractTerrain terrain;

  TerrainEnum(int value, AbstractTerrain terrain) {
    this.value = value;
    this.terrain = terrain;
  }

  public static TerrainEnum getTerrainEnum(int i) {
    switch (i) {
      case 0:   return lowland;
      case 1:   return sea;
      case 2:   return beach;
      case 3:   return reef;
      case 5:   return hill;
      case 6:   return mountain;
      case 7:   return river;
      case 8:   return road;
      case 9:   return wood;
      case 10:  return bridge;
      default:  return none;
    }
  }

  public static TerrainEnum getTerrainEnum(AbstractTerrain t) {
  	if (t instanceof Lowland) 	return lowland;
  	if (t instanceof Sea) 		return sea;
  	if (t instanceof Beach) 	return beach;
  	if (t instanceof Reef) 		return reef;
  	if (t instanceof Hill) 		return hill;
  	if (t instanceof Mountain) 	return mountain;
  	if (t instanceof River) 	return river;
  	if (t instanceof Road) 		return road;
  	if (t instanceof Wood) 		return wood;
  	if (t instanceof Bridge) 	return bridge;
  	return none;
  }
}
