package fr.main.view.controllers;

import fr.main.model.Universe;
import fr.main.view.views.OptionView;
import fr.main.view.views.View;
import fr.main.view.MainFrame;

import java.awt.event.ActionListener;

/**
 * Option panel
 */
public class OptionController extends Controller {

	public ActionListener quit;

    public OptionController(){
        super();

        quit = e -> MainFrame.setScene(new MenuController());
    }

    public View makeView(){
        return new OptionView(this);
    }
}