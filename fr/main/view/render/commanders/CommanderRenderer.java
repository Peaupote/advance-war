package fr.main.view.render.commanders;

import java.util.*;
import java.awt.*;

import fr.main.view.render.Renderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class CommanderRenderer {

  private static final HashMap<String, Render> renderers = new HashMap<>();

  public static class Render extends Renderer {

    protected Animation anim;

    public Render () {
      anim = new Animation();
    }

    public Render (String commander) {
      anim = new Animation();

      LinkedList<ScaleRect> areas = new LinkedList<>();

      areas.add(new ScaleRect(0, 168, 35, 35));
      areas.add(new ScaleRect(37, 168, 35, 35));

      String path = "./assets/commanders/" + commander + ".png";
      AnimationState good = new AnimationState(new SpriteList(path, areas), 50);

      for (ScaleRect rect: areas)
        rect.y = 206;
      AnimationState bad  = new AnimationState(new SpriteList(path, areas), 50);

      anim.put("good", good);
      anim.put("bad", bad);
      anim.setState("good");
    }

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x, y);
    }
  }

    public static Render getRender (String commander) {
      if (!renderers.containsKey(commander)) 
        renderers.put(commander, new Render(commander));

      return renderers.get(commander);
    }

    public static void render (Graphics g, String commander, Point pt) {
      getRender(commander).draw(g, pt.x, pt.y);
    }

}
