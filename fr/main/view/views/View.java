package fr.main.view.views;

import javax.swing.JPanel;

import fr.main.view.controllers.Controller;

/**
 * Empty view
 */
public class View extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3106932721264035758L;

	public View (Controller controller) {
        addKeyListener(controller);
        addMouseMotionListener(controller);
        addMouseListener(controller);
        addMouseWheelListener(controller);
    }

}
