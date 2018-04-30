package fr.main.view.controllers;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ListSelectionListener;

import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.generator.MapGenerator;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.views.MapView;

public class MapController extends Controller {

    public final DefaultListModel<String> listModel;
    public final ListSelectionListener list;
    public final ActionListener start;
    private final Player[] ps;
    private MapView view;

    @SuppressWarnings("serial")
    public class RandomSettings extends JDialog {

        private MapGenerator gen;
        @SuppressWarnings("unused")
		private JSlider width, height, seed, land, moutain, wood, barrackNb, cityRingNb, airportNb, dockNb;
        private JButton ok;
        private JCheckBox silo, startDocks, startBarracks, startAirport;
        public boolean cancel = true;

        public RandomSettings (Frame parent, String title, boolean modal) {
            super(parent, title, modal);
            gen = new MapGenerator(10, ps.length);

            width   = new JSlider(JSlider.HORIZONTAL, 20, 60, 30);
            gen.setMapWidth(width.getValue());
            height  = new JSlider(JSlider.HORIZONTAL, 20, 60, 30);
            gen.setMapHeight(height.getValue());
            seed    = new JSlider(JSlider.HORIZONTAL, Integer.MIN_VALUE, Integer.MAX_VALUE, (new Random()).nextInt());
            gen.setSeed(seed.getValue());
            land    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            gen.setLandProportion(land.getValue());
            moutain = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            wood    = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
            gen.setMountainProportion(moutain.getValue());
			cityRingNb = new JSlider(JSlider.HORIZONTAL, 0, 6, 2);
			gen.setCityRingNb(cityRingNb.getValue());
			barrackNb = new JSlider(JSlider.HORIZONTAL, 0, 12, 2);
			gen.setBarracksNb(barrackNb.getValue());
			airportNb = new JSlider(JSlider.HORIZONTAL, 0, 6, 2);
			gen.setAirportNb(airportNb.getValue());
//			dockNb = new JSlider(JSlider.HORIZONTAL, 0, 6, 2);
//			gen.setDocksNb(dockNb.getValue());

			silo = new JCheckBox();
			gen.setPlaceSilo(silo.isSelected());

			startAirport = new JCheckBox();
			gen.setStarterDock(startAirport.isSelected());

			startBarracks = new JCheckBox();
			startBarracks.setSelected(true);
			gen.setStarterBarrack(startBarracks.isSelected());

			startDocks = new JCheckBox();
			gen.setStarterDock(startDocks.isSelected());


            ok = new JButton("Done");

            setSize(new Dimension(250, 800));

            setLayout(new GridLayout(30, 1, 10, 10));
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

            add(new JLabel("Number of Cities to capture/Player"));
            add(cityRingNb);

            add(new JLabel("Available Barracks"));
            add(barrackNb);

            add(new JLabel("Available Airports"));
            add(airportNb);

//            add(new JLabel("Available Docks"));
//            add(dockNb);

			add(new JLabel("Start Barracks"));
			add(startBarracks);

			add(new JLabel("Start Airport"));
			add(startAirport);

			add(new JLabel("Start Docks"));
			add(startDocks);

            add(new JLabel("Add Missile Silo"));
            add(silo);

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