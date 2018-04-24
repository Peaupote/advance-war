package fr.main.view.render.units.naval;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.sound.MusicEngine;

public class BattleshipRenderer extends UnitRenderer.Render {

    public BattleshipRenderer (AbstractUnit unit) {
        super (unit);

        LinkedList<ScaleRect> areas = new LinkedList<>();
        areas.add(new ScaleRect (119, 4, 16, 15, 2));
        areas.add(new ScaleRect (139, 4, 16, 15, 2));
        AnimationState idle = new AnimationState(new SpriteList(getDir() + "sea.png", areas), 30);

        anim.put("idleRIGHT", idle);
        anim.setState("idleRIGHT");

        this.selected = new MusicEngine("./assets/sound/song056.wav");
        this.attack = new MusicEngine("./assets/sound/song054.wav");
    }

}