package fr.main.view.render.terrains.naval;

import fr.main.view.render.terrains.TerrainLocation;
import fr.main.view.render.terrains.TerrainRenderer;

public class SeaRenderer extends TerrainRenderer.Render {

	public SeaRenderer(TerrainLocation.SeaLocation location) {
		super(location);
//
//    LinkedList<ScaleRect> areas = new LinkedList<>();
//    areas.add(new ScaleRect (17, 17, 16, 16, 2));
//    AnimationState idle = new AnimationState(new SpriteList("./assets/terrains/beach1.png", areas), 20);
//    anim.put("idle", idle);
//    anim.setState("idle");
	}

	public SeaRenderer(TerrainLocation.GenericTerrainLocation location) {
		super(location);
	}

}

