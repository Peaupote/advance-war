package fr.main.view.render.weather;

import java.awt.*;
import java.util.*;

import fr.main.view.MainFrame;

public abstract class WeatherRender {

    protected static final double radius = Math.toRadians(20),
                                  dx     = Math.sin(radius),
                                  dy     = Math.cos(radius);


    protected static Random rand = new Random();

    private LinkedList<Particule> particules;

    protected abstract class Particule {
        int x, y, incrX, incrY;

        public Particule () {
            backTop();
        }

        public void backTop () {
            double speed = rand.nextInt(15) + 10;
            incrX        = (int)(dx * speed) + 1;
            incrY        = (int)(dy * speed) + 1;
            x            = rand.nextInt(MainFrame.width() + 1) - rand.nextInt(MainFrame.width() + 1);
            y            = -rand.nextInt(200);
        }

        public void render (Graphics g) {
            draw(g);

            x += incrX;
            y += incrY;
            if (y > MainFrame.height()) backTop();
        }

        protected abstract void draw (Graphics g);

    }

    public WeatherRender (int density) {
        particules = new LinkedList<>();
        
        for (int i = 0; i < density; i++)
            particules.add(createParticle());
    }

    protected abstract Particule createParticle ();

    public final void render (Graphics g) {
        for (Particule particle: particules)
            particle.render(g);
    }

}
