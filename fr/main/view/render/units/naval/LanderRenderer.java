package fr.main.view.render.units.naval;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.sound.MusicEngine;

public class LanderRenderer extends UnitRenderer.Render {

    public LanderRenderer (AbstractUnit unit) {
        super(unit);
        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect(9, 3, 16, 16, 2));
        areas.add(new ScaleRect(27, 3, 16, 16, 2));
        AnimationState idle = new AnimationState (new SpriteList(getDir() + "sea.png", areas), 30);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(7, 60, 18, 13, 2));
        areas.add(new ScaleRect(27, 60, 18, 13, 2));
        AnimationState move = new AnimationState (new SpriteList(getDir() + "sea.png", areas), 30);

        anim.put("idleRIGHT", idle);
        anim.put("moveRIGHT", move);
        anim.setState("idleRIGHT");

        this.selected = new MusicEngine("./assets/sound/song056.wav");
        this.attack = new MusicEngine("./assets/sound/song054.wav");
    }

}

