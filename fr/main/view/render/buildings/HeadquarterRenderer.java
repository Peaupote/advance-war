package fr.main.view.render.buildings;

import java.util.LinkedList;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;

public class HeadquarterRenderer extends OwnableBuildingRenderer {

    public HeadquarterRenderer (AbstractBuilding building) {
        super(building);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (0, 0, 16, 31, 2));
        areas.add(new ScaleRect (19, 0, 16, 31, 2));
        AnimationState red = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (43, 36, 16, 31, 2));
        areas.add(new ScaleRect (62, 36, 16, 31, 2));
        AnimationState blue = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (86, 71, 16, 27, 2));
        areas.add(new ScaleRect (105, 71, 16, 27, 2));
        AnimationState green = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (127, 102, 16, 30, 2));
        areas.add(new ScaleRect (146, 102, 16, 30, 2));
        AnimationState yellow = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        anim.put("red", red);
        anim.put("blue", blue);
        anim.put("green", green);
        anim.put("yellow", yellow);

        super.updateState();
    }

    public void updateState(){
        BuildingRenderer.renderers.remove(building);
        BuildingRenderer.updateAll();
    }
}