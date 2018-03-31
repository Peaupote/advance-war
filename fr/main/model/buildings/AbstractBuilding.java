package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.AbstractTerrain;

/**
 * Interface representing a building
 *
 */
public interface AbstractBuilding extends java.io.Serializable{
  
  /**
   * @return the name of the building
   */
  String getName();

  /**
   * @param u is the unit on the building
   * @return defense bonus given by the building
   */
  int getDefense (AbstractUnit u);

  /**
   * @return terrain on which is the building
   */
  AbstractTerrain getTerrain ();

  int getX();
  int getY();

}

