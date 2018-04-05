package fr.main.view.controllers;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import fr.main.view.views.View;

/**
 * Super controller class
 */
public abstract class Controller
  extends KeyAdapter implements MouseMotionListener {

  /**
   * Function called each loop turn.
   */
  public void update() {}

  /**
   * Create a new view for the controller
   */
  public abstract View makeView ();

  /**
   * Key Events
   * By default doing nothing
   */

  @Override
  public void keyPressed (KeyEvent e) {}

  @Override
  public void keyReleased (KeyEvent e) {}

  @Override
  public void keyTyped(KeyEvent e) {}

  /**
   * Mouse events 
   * By default doing nothing
   */

  @Override
  public void mouseDragged (MouseEvent e) {}

  @Override
  public void mouseMoved (MouseEvent e) {}
}
