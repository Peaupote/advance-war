package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

public interface AbstractBuilding {
    int getX();
    int getY();
    int getDefense();
    String getName();
    Terrain getTerrain();
}
