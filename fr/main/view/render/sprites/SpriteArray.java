package fr.main.view.render.sprites;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class SpriteArray extends ArrayList<Image> {

    public SpriteArray(String path, LinkedList<ScaleRect> images) {
        super();

        Sprite sprite = Sprite.get(path);
        for (ScaleRect rect : images)
        add(sprite.getImage(rect));
    }

}

