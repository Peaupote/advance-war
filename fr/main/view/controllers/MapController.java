package fr.main.view.controllers;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.nio.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.Stream;
import java.util.Random;

import fr.main.model.TerrainEnum;
import fr.main.model.generator.MapGenerator;
import fr.main.model.players.Player;
import fr.main.view.views.MapView;
import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.generator.MapGenerator;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;

public class MapController extends Controller {

    public final DefaultListModel<String> listModel;
    public final ListSelectionListener list;
    public final ActionListener start;
    private final Player[] ps;
    private MapView view;

    @SuppressWarnings("serial")
    public class RandomSettings extends JDialog {

        private MapGenerator gen;
        private JSlider width, height, seed, land, moutain, wood;
        private JButton ok;
        public boolean cancel = true;

        public RandomSettings (Frame parent, String title, boolean modal) {
            super(parent, title, modal);
            gen = new MapGenerator(10, ps.length);

            width   = new JSlider(JSlider.HORIZONTAL, 100, 500, 100);
            gen.setMapWidth(width.getValue());
            height  = new JSlider(JSlider.HORIZONTAL, 100, 500, 100);
            gen.setMapHeight(height.getValue());
            seed    = new JSlider(JSlider.HORIZONTAL, Integer.MIN_VALUE, Integer.MAX_VALUE, (new Random()).nextInt());
            gen.setSeed(seed.getValue());
            land    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            gen.setLandProportion(land.getValue());
            moutain = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            wood    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            gen.setMountainProportion(moutain.getValue());

            ok = new JButton("Done");

            setSize(new Dimension(250, 500));

            setLayout(new GridLayout(14, 1, 10, 10));
            add(new JLabel("Random settings"));

            add(new JLabel("Seed"));
            add(seed);

            add(new JLabel("Width"));
            add(width);

            add(new JLabel("Height"));
            add(height);

            add(new JLabel("Land/Sea proportion"));
            add(land);

            add(new JLabel("Mountain proportion"));
            add(moutain);

            add(new JLabel("Wood proportion"));
            add(wood);

            add(ok);

            seed.addChangeListener(  e -> gen.setSeed(seed.getValue()));
            width.addChangeListener( e -> gen.setMapWidth(width.getValue()));
            height.addChangeListener(e -> gen.setMapHeight(height.getValue()));

            land.addChangeListener(e -> {
                gen.setLandProportion(land.getValue());
            });

            moutain.addChangeListener(e -> {
                gen.setMountainProportion(moutain.getValue());
                wood.setValue(gen.getWoodProportion());
            });

            wood.addChangeListener(e -> {
                gen.setWoodProportion(wood.getValue());
                moutain.setValue(gen.getMountainProportion());
            });

            ok.addActionListener(e -> {
                cancel = false;
                setVisible(false);
            });
        }

        public MapGenerator showRandomSettings () {
            setVisible(true);
            return gen;
        }
    }

    public MapController (Player[] ps) {
        this.ps = ps;

        listModel = new DefaultListModel<>();

        try {
            try (Stream<Path> paths = Files.walk(Paths.get(Universe.mapPath))) {
                paths.filter(Files::isRegularFile)
                .forEach(file -> listModel.addElement(file.toString().substring(Universe.mapPath.length())));
            }
        } catch (IOException e) {
            System.err.println("Can't find " + Universe.mapPath + " folder");
        }

        listModel.addElement("Custom Map");

        list = e -> view.start.setEnabled(!view.list.isSelectionEmpty());
        start = e -> {
            int index = view.list.getMaxSelectionIndex();
            String mapPath = "";
            if (index == listModel.size() - 1) {
                RandomSettings r = new RandomSettings(MainFrame.instance, "Random Settings", true);
                MapGenerator mGen = r.showRandomSettings();
                if (r.cancel) return;

                int height = mGen.getMapHeight(),
                    width  = mGen.getMapWidth();

                mGen.randMap(height, width);
                TerrainEnum[][] eMap           = mGen.getLastMap();
                Player[] players               = mGen.getLastPlayers();
                AbstractBuilding[][] buildings = mGen.getLastBuildingLayout();
                AbstractTerrain[][] map        = new AbstractTerrain[height][width];

                for (int i = 0; i < eMap.length; i++)
                        for (int j = 0; j < eMap[0].length; j++)
                                map[i][j] = eMap[i][j].terrain;

                mapPath = "custom.map";
                Universe.save(mapPath, new AbstractUnit[height][width], map, players, buildings);
            } else mapPath = listModel.get(index);

            MainFrame.setScene(new GameController(mapPath, ps));
        };
}

    public MapView makeView() {
        view = new MapView(this);
        return view;
    }

}