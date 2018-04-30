package fr.main.view.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import fr.main.view.controllers.EditorController;

@SuppressWarnings("serial")
public class EditorView extends View {

    private EditorController controller;

    boolean isListening = false;

    public class Map extends JPanel {

        public void paintComponent (Graphics g) {
            // TODO: show how the land will be in the map
            controller.world.draw(g, controller.camera.getX(), 
                    controller.camera.getY(), 
                    controller.camera.getOffsetX(),
                    controller.camera.getOffsetY());

            g.setColor(Color.black);
            for (int i = 0; i < 4; i++)
                g.fillPolygon(controller.arrows[i][0], controller.arrows[i][1], 3);
        }

    }

    @SuppressWarnings("serial")
    private class Tools extends JPanel {

        JSlider width, height, seed, land, moutain, wood;
        JButton[] lands;
        JButton save, open, menu;
        
        public Tools () {
            setLayout(new GridLayout(1, 2));

            JPanel tools = new JPanel();
            tools.setLayout(new GridLayout(16, 1));
            tools.add(new JLabel("Add tools here"));

            width  = new JSlider(JSlider.HORIZONTAL, 15, 500, 50);
            height = new JSlider(JSlider.HORIZONTAL, 15, 500, 50);
            seed   = new JSlider(JSlider.HORIZONTAL, 10, 500, 50);
            land    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            moutain = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            wood    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);

            save = new JButton("Save");
            open = new JButton("Open");
            menu = new JButton("Back to menu");

            tools.add(new JLabel("Width:"));
            tools.add(width);

            tools.add(new JLabel("Height:"));
            tools.add(height);

            tools.add(new JLabel("Seed:"));
            tools.add(seed);


            tools.add(new JLabel("Land/Sea proportion:"));
            tools.add(land);

            tools.add(new JLabel("Mountain proportion:"));
            tools.add(moutain);


            tools.add(new JLabel("Wood proportion:"));
            tools.add(wood);

            tools.add(save);
            tools.add(open);
            tools.add(menu);

            JPanel terrains = new JPanel();
            terrains.setLayout(new GridLayout(11, 1));
            terrains.add(new JLabel("Change terrains"));

            lands = new JButton[10];
            lands[0] = new JButton("Beach");
            lands[1] = new JButton("Bridge");
            lands[2] = new JButton("Hill");
            lands[3] = new JButton("Lowland");
            lands[4] = new JButton("Mountain");
            lands[5] = new JButton("River");
            lands[6] = new JButton("Road");
            lands[7] = new JButton("Wood");
            lands[8] = new JButton("Reef");
            lands[9] = new JButton("Sea");

            for (int i = 0; i < lands.length; i++)
                terrains.add(lands[i]);

            controller.new TerrainListener (lands);

            add(terrains);
            add(tools);
        }
    }

    public final Map map;
    private Tools tools;

    public EditorView (EditorController controller) {
        super(controller);
        this.controller = controller;

        map = new Map ();
        tools = new Tools();

        setLayout(new BorderLayout());
        add(map, BorderLayout.CENTER);
        add(tools, BorderLayout.EAST);

        controller.new Adaptater(tools.width, tools.height, tools.seed, tools.land, tools.moutain, tools.wood);
        map.addMouseMotionListener(controller);
        map.addMouseListener(controller);

        tools.save.addActionListener(controller.save);
        tools.open.addActionListener(controller.open);
        tools.menu.addActionListener(controller.menu);
    }

}
