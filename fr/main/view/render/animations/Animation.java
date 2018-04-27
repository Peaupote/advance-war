package fr.main.view.render.animations;

import java.awt.Graphics;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Animation extends HashMap<String, AnimationState> {

    private AnimationState current;

    public void setState (String state) {
        if (containsKey(state))
            current = get(state);
    }

    public void draw(Graphics g, int x, int y) {
        current.draw(g, x, y);
    }

}
