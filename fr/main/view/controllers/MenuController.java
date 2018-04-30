package fr.main.view.controllers;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.main.model.Universe;
import fr.main.view.views.*;
import fr.main.view.MainFrame;
import fr.main.network.*;
import fr.main.view.sound.MusicEngine;

/**
 * Main Menu.
 */
public class MenuController extends Controller {
    
    public final ActionListener play,
                                exit,
                                edit,
                                host,
                                join,
                                option,
                                load;

    private boolean listen;
    public MusicEngine bm = new MusicEngine("./assets/sound/main.wav");

    public class Music implements ActionListener {
     
        private final JButton sound;

        public Music (JButton enableMusic) {
            enableMusic.addActionListener(this);
            sound = enableMusic;
        }

        public void actionPerformed (ActionEvent e) {
        	listen = !listen;
            if(!listen) {
                sound.setIcon(new ImageIcon("./assets/button/music03.png"));
                bm.continues();
            } else {
                sound.setIcon(new ImageIcon("./assets/button/music02.png"));
                bm.stop();
            }
        }
    }

    public MenuController () {
        super();

        play   = e -> MainFrame.setScene(new CreateController());

        option = e -> MainFrame.setScene(new OptionController());
        edit   = e -> MainFrame.setScene(new EditorController());

        exit   = e -> System.exit(0);

        load   = e -> {
            JFileChooser jfc = new JFileChooser(Universe.mapPath);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("Only .map files are accepted", "map"));
            if (jfc.showOpenDialog(MainFrame.instance) == JFileChooser.APPROVE_OPTION){
                try{
                    MainFrame.setScene(new LoadController(jfc.getSelectedFile().getName()));
                }catch(Exception exc){}
            }
        };

        host   = e -> {
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
        join   = e -> MainFrame.setScene(new ConnectionController());
    }

    public View makeView () {
        return new MenuView(this);
    }

}
