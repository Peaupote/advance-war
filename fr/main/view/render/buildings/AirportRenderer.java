package fr.main.view.render.buildings;

import java.util.LinkedList;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;

public class AirportRenderer extends OwnableBuildingRenderer {

    public AirportRenderer (AbstractBuilding building) {
        super(building);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (168, 180, 16, 18, 2));
        AnimationState white = new AnimationState (new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (0, 181, 16, 18, 2));
        areas.add(new ScaleRect (19, 181, 16, 18, 2));
        AnimationState red = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (43, 181, 16, 18, 2));
        areas.add(new ScaleRect (62, 181, 16, 18, 2));
        AnimationState blue = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (86, 181, 16, 18, 2));
        areas.add(new ScaleRect (105, 181, 16, 18, 2));
        AnimationState green = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (127, 181, 16, 18, 2));
        areas.add(new ScaleRect (146, 181, 16, 18, 2));
        AnimationState yellow = new AnimationState(new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        anim.put("white", white);
        anim.put("red", red);
        anim.put("blue", blue);
        anim.put("green", green);
        anim.put("yellow", yellow);

        updateState();
    }
}