package fr.main.view.views;

import javax.swing.JPanel;

import fr.main.view.controllers.Controller;

/**
 * Empty view
 */
@SuppressWarnings("serial")
public class View extends JPanel {

	public View (Controller controller) {
        addKeyListener(controller);
        addMouseMotionListener(controller);
        addMouseListener(controller);
        addMouseWheelListener(controller);
    }

}
