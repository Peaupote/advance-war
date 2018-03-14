package fr.main.view.render.terrains;

import fr.main.view.render.terrains.TerrainImage;

import java.io.Serializable;

public interface TerrainLocation extends Serializable{
	public String getPath();
	public TerrainImage.Location location();
}
