package fr.main.view.controllers;

import fr.main.view.views.LoadView;
import fr.main.model.players.Player;
import fr.main.view.MainFrame;

/**
 * Loading screen
 * TODO: load stuffs.
 */
public class LoadController extends Controller {

    private int load;
    private GameController controller;
    private boolean ready;

    public LoadController(String map, Player[] ps){
        controller = null;
        ready = false;
        new Thread(() -> {
          controller = new GameController(map, ps);
          ready = true;
        }).start();
        load = 0;
    }

    public LoadController(String filename) {
        ready = false;
        controller = null;
        new Thread(() -> {
          controller = new GameController(filename);
          ready = true;
        }).start();
        load = 0;
    }

    public void update() {
        load = Math.min(500, load + 1);

        if (load >= 500 && ready) MainFrame.setScene(controller);
    }

    public int getLoad () {
        return load / 5;
    }

    public LoadView makeView () {
        return new LoadView(this);
    }

}
