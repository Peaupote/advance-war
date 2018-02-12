package fr.main.view.interfaces;

import java.util.LinkedList;
import java.util.Iterator;
import java.lang.Iterable;
import java.awt.Graphics;

public abstract class InterfaceUI {

  private boolean visible;

  private static class ComponentsIt implements Iterable<InterfaceUI> {

    private LinkedList<InterfaceUI> components;

    public ComponentsIt () {
      components = new LinkedList<>();
    }

    public Iterator<InterfaceUI> iterator() {
      return components.iterator();
    }

  }

  private static ComponentsIt comps = new ComponentsIt();

  public static Iterable<InterfaceUI> components () {
    return comps;
  }

  public InterfaceUI (boolean visible) {
    this.visible = visible;
    comps.components.add(this);
  }

  public InterfaceUI () {
    this(true);
  }

  protected abstract void draw (Graphics g);

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

