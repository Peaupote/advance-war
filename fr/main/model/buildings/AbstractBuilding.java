package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.AbstractTerrain;

public interface AbstractBuilding extends java.io.Serializable{
  
  String getName();
  int getDefense (AbstractUnit u);
  AbstractTerrain getTerrain ();
  int getX();
  int getY();

}

