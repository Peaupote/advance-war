package fr.main.view.controllers;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import fr.main.view.views.View;

public abstract class Controller
  extends KeyAdapter implements MouseMotionListener {

  /**
   * Function called each loop turn
   */
  public abstract void update();
  public abstract View makeView ();

  @Override
  public void keyPressed (KeyEvent e) {}

  @Override
  public void keyReleased (KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void mouseDragged (MouseEvent e) {}

  @Override
  public void mouseMoved (MouseEvent e) {}
}
