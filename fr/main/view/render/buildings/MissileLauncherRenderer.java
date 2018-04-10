package fr.main.view.render.buildings;

import java.util.LinkedList;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.MissileLauncher;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;

public class MissileLauncherRenderer extends BuildingRenderer.BuildingRender {

    public MissileLauncherRenderer (AbstractBuilding building) {
        super(building);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (168, 225, 16, 23, 2));
        AnimationState active = new AnimationState (new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect (187, 234, 16, 15, 2));
        AnimationState inactive = new AnimationState (new SpriteList("./assets/buildings/colored_buildings.png", areas), 50);

        anim.put("active", active);
        anim.put("inactive", inactive);

        updateState();
    }

    public void updateState(){
        anim.setState(((MissileLauncher)building).isFired() ? "inactive" : "active");
    }

    public void updateState(String s){
        if (s.equals("active") || s.equals("inactive"))
            anim.setState(s);
        else 
            updateState();
    }
}