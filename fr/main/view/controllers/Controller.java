package fr.main.view.controllers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import fr.main.view.views.View;

/**
 * Super controller class.
 */
public abstract class Controller
    extends KeyAdapter implements MouseMotionListener, MouseListener, MouseWheelListener {

    /**
     * Function called each loop turn.
     */
    public void update() {}

    /**
     * Create a new view for the controller.
     */
    public abstract View makeView ();

    /**
     * Key Events
     * By default doing nothing.
     */

    @Override
    public void keyPressed (KeyEvent e) {}

    @Override
    public void keyReleased (KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Mouse events 
     * By default doing nothing.
     */

    @Override
    public void mouseDragged (MouseEvent e) {}
    @Override
    public void mouseMoved (MouseEvent e) {}

    @Override
    public void mouseClicked (MouseEvent e) {}
    @Override
    public void mousePressed (MouseEvent e) {}
    @Override
    public void mouseEntered (MouseEvent e) {}
    @Override
    public void mouseExited (MouseEvent e) {}
    @Override
    public void mouseReleased (MouseEvent e) {}

    @Override
    public void mouseWheelMoved (MouseWheelEvent e){}

    public void onOpen () {}
    public void onClose () {}
}
