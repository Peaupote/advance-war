package fr.main.view.render.units.air;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.sound.MusicEngine;

public class StealthRenderer extends UnitRenderer.Render {

    public StealthRenderer (AbstractUnit unit) {
        super (unit);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (32, 0, 16, 16, 2));
        AnimationState idle = new AnimationState(new SpriteList(getDir() + "missing.png", areas), 20);

        anim.put("idleRIGHT", idle);
        anim.setState("idleRIGHT");

        this.selected = new MusicEngine("./assets/sound/song022.wav");
        this.attack = new MusicEngine("./assets/sound/song057.wav");
    }

}