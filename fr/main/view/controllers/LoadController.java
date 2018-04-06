package fr.main.view.controllers;

import fr.main.view.views.LoadView;
import fr.main.model.players.Player;
import fr.main.view.MainFrame;

/**
 * Loading screen
 * TODO: load stuffs
 */
public class LoadController extends Controller {

    private int load;
    private final GameController controller;

    public LoadController(GameController g){
        this.controller = g;
        load = 0;
    }

    public void update() {
        load++;

        if (load == 100) MainFrame.setScene(controller);
    }

    public int getLoad () {
        return load;
    }

    public LoadView makeView () {
        return new LoadView(this);
    }

}
