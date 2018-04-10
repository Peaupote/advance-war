package fr.main.view.render.animations;

import java.awt.Graphics;
import java.util.HashMap;

public class Animation extends HashMap<String, AnimationState> {

  /**
	 * Add Animation UID
	 */
	private static final long serialVersionUID = -8517367548023000613L;
private AnimationState current;

  public void setState (String state) {
    if (containsKey(state))
      current = get(state);
  }

  public void draw(Graphics g, int x, int y) {
    current.draw(g, x, y);
  }

}
