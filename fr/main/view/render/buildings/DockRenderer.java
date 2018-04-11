package fr.main.view.render.buildings;

import java.util.LinkedList;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;

public class DockRenderer extends OwnableBuildingRenderer {

    public DockRenderer (AbstractBuilding building) {
        super(building);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (169, 201, 16, 21, 2));
        AnimationState white = new AnimationState (new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (0, 202, 16, 21, 2));
        areas.add(new ScaleRect (19, 202, 16, 21, 2));
        AnimationState red = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (43, 202, 16, 21, 2));
        areas.add(new ScaleRect (62, 202, 16, 21, 2));
        AnimationState blue = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (86, 202, 16, 21, 2));
        areas.add(new ScaleRect (105, 202, 16, 21, 2));
        AnimationState green = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (127, 202, 16, 21, 2));
        areas.add(new ScaleRect (146, 202, 16, 21, 2));
        AnimationState yellow = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        anim.put("white", white);
        anim.put("red", red);
        anim.put("blue", blue);
        anim.put("green", green);
        anim.put("yellow", yellow);

        updateState();
    }
}