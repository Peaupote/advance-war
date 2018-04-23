package fr.main.view.interfaces;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 * User interface panels.
 */
public abstract class InterfaceUI {

  private boolean visible;

  /**
   * List of all interfaces.
   */
  private static LinkedList<InterfaceUI> comps = new LinkedList<>();

  public static void clear () {
    comps.clear();
  }

  public static Iterable<InterfaceUI> components () {
    return comps;
  }

  public InterfaceUI (boolean visible) {
    this.visible = visible;
    comps.add(this);
  }

  public InterfaceUI () {
    this(true);
  }

  protected abstract void draw (Graphics g);

  /**
   * Render interface visual if visible.
   */
  public final void render (Graphics g) {
    if (visible) draw(g);
  }
  
  public final boolean isVisible () {
    return visible;
  }

  public final void setVisible (boolean visible) {
    this.visible = visible;
    if (visible) onOpen();
    else onClose();
  }

  protected void onOpen() {}
  protected void onClose() {}

}

