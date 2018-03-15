package fr.main.view.render.buildings;

import java.util.LinkedList;
import java.awt.Color;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class CityRenderer extends OwnableBuildingRenderer {

    public CityRenderer (AbstractBuilding building) {
        super(building);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (168, 137, 16, 20, 2));
        AnimationState white = new AnimationState (new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (0, 137, 16, 20, 2));
        areas.add(new ScaleRect (19, 137, 16, 20, 2));
        AnimationState red = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (43, 137, 16, 20, 2));
        areas.add(new ScaleRect (62, 137, 16, 20, 2));
        AnimationState blue = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (86, 137, 16, 20, 2));
        areas.add(new ScaleRect (105, 137, 16, 20, 2));
        AnimationState green = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (127, 137, 16, 20, 2));
        areas.add(new ScaleRect (146, 137, 16, 20, 2));
        AnimationState yellow = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        anim.put("white", white);
        anim.put("red", red);
        anim.put("blue", blue);
        anim.put("green", green);
        anim.put("yellow", yellow);

        updateState();
    }
}