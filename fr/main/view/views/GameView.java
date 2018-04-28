package fr.main.view.views;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import fr.main.view.controllers.GameController;
import fr.main.view.interfaces.InterfaceUI;
import fr.main.view.render.weather.WeatherController;

@SuppressWarnings("serial")
public class GameView extends View {

    /**
    * Rendering weather controller
    */
    private final WeatherController weather;
    protected GameController controller;

    public GameView (GameController controller) {
        super (controller);
        this.controller = controller;
        this.weather = new WeatherController();

        // remove mouse display
        setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
    }

    @Override
    public void paintComponent (Graphics g) {
        int x = controller.camera.getX(),
            y = controller.camera.getY(),
            offsetX = controller.camera.getOffsetX(),
            offsetY = controller.camera.getOffsetY();

        // render map
        controller.world.draw(g, x, y, offsetX, offsetY);

        // render cursor
        if (controller.getMode() != GameController.Mode.MOVE &&
                controller.getMode() != GameController.Mode.RANGE)
            controller.unitCursor.draw(g);
        else controller.cursor.draw(g);

        // render path
        if (controller.path.visible)
            controller.path.draw(g, offsetX, offsetY);

        // render damages
        controller.displayDamages(g);

        // render weather
        weather.render(g);

        // render user interface
        for (InterfaceUI comp: InterfaceUI.components())
            comp.render(g);
    }

    public WeatherController getWeatherController(){
        return weather;
    }
}
