package fr.main.view;

import java.util.LinkedList;
import javax.swing.JPanel;
import java.awt.*;

import fr.main.view.interfaces.*;
import fr.main.view.render.weather.*;

public class View extends JPanel {

    private final Controller controller;
    private final WeatherController weather;

    public View (Controller controller) {
        this.controller = controller;
        this.weather = new Snow(100);

        addKeyListener(controller);
        addMouseMotionListener(controller);
    }

    @Override
    public void paintComponent (Graphics g) {
        int x = controller.camera.getX(),
                y = controller.camera.getY(),
                offsetX = controller.camera.getOffsetX(),
                offsetY = controller.camera.getOffsetY();
        controller.world.draw(g, x, y, offsetX, offsetY);
        
        if (controller.getMode() != Controller.Mode.MOVE) controller.unitCursor.draw(g);
        else controller.cursor.draw(g);

        if (controller.path.visible) controller.path.draw(g, offsetX, offsetY);
        
        weather.render(g);

        for (InterfaceUI comp: InterfaceUI.components())
            comp.render(g);
    }

}
