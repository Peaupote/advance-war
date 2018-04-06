package fr.main.view.views;

import java.util.LinkedList;
import javax.swing.JPanel;
import java.awt.*;

import fr.main.view.controllers.Controller;

/**
 * Empty view
 */
public class View extends JPanel {

    public View (Controller controller) {
        addKeyListener(controller);
        addMouseMotionListener(controller);
        addMouseListener(controller);
    }

}
