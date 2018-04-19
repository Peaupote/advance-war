package fr.main.view.controllers;

import java.awt.event.ActionListener;

import fr.main.view.MainFrame;
import fr.main.view.views.OptionView;
import fr.main.view.views.View;

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