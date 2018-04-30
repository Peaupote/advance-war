package fr.main.view.render.units.land;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;
import fr.main.view.sound.MusicEngine;

public class TankRenderer extends UnitRenderer.Render {

    public TankRenderer (AbstractUnit unit) {
        super (unit);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (452, 286, 15, 13, 2));
        areas.add(new ScaleRect (472, 286, 15, 13, 2));
        AnimationState idle = new AnimationState(new SpriteList(getDir() + "sprites.png", areas), 20);

        anim.put("idleRIGHT", idle);
        anim.setState("idleRIGHT");

        this.selected = new MusicEngine("./assets/sound/ShermanE8.wav");
        this.attack = new MusicEngine("./assets/sound/TankShot.wav");
    }

}