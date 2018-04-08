package fr.main.view.controllers;

import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.main.model.Universe;
import fr.main.view.views.*;
import fr.main.view.MainFrame;
import fr.main.network.*;

/**
 * Main Menu
 */
public class MenuController extends Controller {
    
    public final ActionListener play,
                                exit,
                                host,
                                join,
                                load;

    public MenuController () {
        super();

        play = e -> MainFrame.setScene(new CreateController());

        exit = e -> System.exit(0);

        load = e -> {
            JFileChooser jfc = new JFileChooser(Universe.mapPath);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("Only .map files are accepted", "map"));
            if (jfc.showOpenDialog(MainFrame.instance) == JFileChooser.APPROVE_OPTION){
                try{
                    MainFrame.setScene(new LoadController(new GameController(jfc.getSelectedFile().getName())));
                }catch(Exception exc){}
            }
        };

        host = e -> {
            try {
                Server server = new Server(8080);
                new Thread(server::listen).start();

                Client client = new Client("localhost", 8080);

                MainFrame.setScene(new HubController.Host(client, server));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println(ex);
                ex.printStackTrace();
            }
        };
        join = e -> MainFrame.setScene(new ConnectionController());
    }

    public View makeView () {
        return new MenuView(this);
    }

}
