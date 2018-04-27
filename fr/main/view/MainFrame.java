package fr.main.view;

import java.awt.Dimension;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import fr.main.view.controllers.Controller;
import fr.main.view.controllers.MenuController;
import fr.main.view.views.View;

/**
 * Frame for the application.
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    /**
     * Dimension of the window.
     */
    public static final int WIDTH = 960,
                            HEIGHT = 704,
                            UNIT = 32;

    /**
     * Time spend since the controller has been
     * loaded (in number of loop turn).
     */
    private static int timer = 0;

    /**
     * Unique instance of the MainFrame.
     */
    public static MainFrame instance;

    /**
     * In-game camera to adjust his size on frame size.
     */
    private static Position.Camera camera = null;

    /**
     * Current controller.
     */
    Controller controller;

    /**
     * View associated with the controller.
     */
    View view;
    
    public MainFrame ()
            throws UnsupportedAudioFileException, IOException,
                         LineUnavailableException {
            super("Advance war");
            instance = this;
            
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(true);
            setMinimumSize(new Dimension(WIDTH, HEIGHT));

            setController(new MenuController());

            // main loop
            
            new Thread(() -> {
                while (true) {
                    timer = timer == Integer.MAX_VALUE ? 0 : timer + 1;
                    controller.update();
                    view.repaint();
                    try {
                            Thread.sleep(10);
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                }
            }).start();
            
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
    }

    public MainFrame (Controller controller) 
            throws Exception {
        this();
        setController(controller);
    }
    
    public static int getTimer () {
            return timer;
    }

    public void setController (Controller controller) {
        if (this.controller != null) this.controller.onClose();
        this.controller = controller;
        controller.onOpen();
        int height = view == null ? HEIGHT : view.getHeight(),
                width  = view == null ? WIDTH  : view.getWidth();
        this.view       = controller.makeView();
        timer           = 0;
            
        setContentPane(view);
        // set view to listen key events
        view.requestFocus(true);
        view.setPreferredSize(new Dimension(width, height));

        pack();
    }

    /**
     * Change Scene.
     */
    public static void setScene (Controller controller) {
        instance.setController (controller);
    }

    public static int width() {
        return instance.view.getWidth();
    }

    public static int height () {
        return instance.view.getHeight();
    }

    public static void setCamera (Position.Camera camera) {
        MainFrame.camera = camera;
    }

    public void validate () {
        super.validate();
        if (camera != null) {
            camera.width = width() / UNIT;
            camera.height = height() / UNIT;
        }
    }

}
