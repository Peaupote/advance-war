package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

public interface AbstractBuilding extends java.io.Serializable{
  
  int getDefense ();
  Terrain getTerrain ();

}

