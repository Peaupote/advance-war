package fr.main.view.render.units.air;

import java.util.LinkedList;

import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;
import fr.main.view.render.units.UnitRenderer;
import fr.main.model.units.AbstractUnit;
import fr.main.view.sound.MusicEngine;

public class FighterRenderer extends UnitRenderer.Render {

    public FighterRenderer (AbstractUnit unit) {
        super (unit);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect(90, 6, 15, 16, 2));
        areas.add(new ScaleRect(111, 7, 15, 16, 2));
        AnimationState idleRight = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(90, 6, 15, 16, 2, ScaleRect.Flip.VERTICALY));
        areas.add(new ScaleRect(111, 7, 15, 16, 2, ScaleRect.Flip.VERTICALY));
        AnimationState idleLeft = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(86, 25, 19, 19, 2));
        areas.add(new ScaleRect(113, 25, 19, 20, 2));
        AnimationState moveRight = new AnimationState(new SpriteList(getDir() + "air.png", areas), 15);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(86, 25, 19, 19, 2, ScaleRect.Flip.VERTICALY));
        areas.add(new ScaleRect(113, 25, 19, 20, 2, ScaleRect.Flip.VERTICALY));
        AnimationState moveLeft = new AnimationState(new SpriteList(getDir() + "air.png", areas), 15);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(90, 50, 15, 20, 2, ScaleRect.Flip.HORIZONTALLY));
        areas.add(new ScaleRect(90, 52, 15, 20, 2, ScaleRect.Flip.HORIZONTALLY));
        AnimationState idleTop = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

        areas = new LinkedList<>();
        areas.add(new ScaleRect(90, 50, 15, 20, 2));
        areas.add(new ScaleRect(90, 52, 15, 20, 2));
        AnimationState idleBottom = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

        anim.put("idleRIGHT", idleRight);
        anim.put("idleLEFT", idleLeft);
        anim.put("idleTOP", idleTop);
        anim.put("idleBOTTOM", idleBottom);

        anim.put("moveRIGHT", moveRight);
        anim.put("moveLEFT", moveLeft);
        anim.setState("idleRIGHT");

        this.selected = new MusicEngine("./assets/sound/song021.wav");
        this.attack = new MusicEngine("./assets/sound/song037.wav");
    }
}
