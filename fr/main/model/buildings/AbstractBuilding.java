package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;

public interface AbstractBuilding extends java.io.Serializable{
  
  int getDefense (AbstractUnit u);
  Terrain getTerrain ();

}

