package fr.main.view.interfaces;

import java.awt.Graphics;

public abstract class InterfaceUI {

  private boolean visible;

  public InterfaceUI (boolean visible) {
    this.visible = visible;
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

