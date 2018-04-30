package fr.main.view.controllers;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import fr.main.model.Direction;
import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.generator.MapGenerator;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.land.Beach;
import fr.main.model.terrains.land.Bridge;
import fr.main.model.terrains.land.Hill;
import fr.main.model.terrains.land.Lowland;
import fr.main.model.terrains.land.Mountain;
import fr.main.model.terrains.land.River;
import fr.main.model.terrains.land.Road;
import fr.main.model.terrains.land.Wood;
import fr.main.model.terrains.naval.Reef;
import fr.main.model.terrains.naval.Sea;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.Position;
import fr.main.view.render.MapRenderer;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.views.EditorView;

public class EditorController extends Controller {

    public MapRenderer world;
    public Position.Camera camera;
    private boolean isListening = false;
    private EditorView view;
    public final ActionListener save, open, menu;

    public static int b = 60,
                      h = b / 3;

    public static final int[][][] refs = new int[][][]{
    { // right
        {0, 0, h},
        {0, b, b/2}
    }, { // bottom
        {0, b/2, b},
        {0, h, 0}
    }, { // top
        {0, b/2, b},
        {h, 0, h}
    }, { // left
        {0, h, h},
        {b/2, 0, b}
    }};

    public final int[][][] arrows = new int[4][2][3];

    public final Rectangle[] arrowButtons;

    private Point mouse = new Point(0,0);
    public int landing = -1;

    public static final int
        BEACH    = 0,
        BRIDGE   = 1,
        HILL     = 2,
        LOWLAND  = 3,
        MOUNTAIN = 4,
        RIVER    = 5,
        ROAD     = 6,
        WOOD     = 7,
        REEF     = 8,
        SEA      = 9;

    private boolean press = false;
    private Point pt;

    public EditorController () {
        arrowButtons = new Rectangle[4];
        for (int i = 0; i < 4; i++)
            arrowButtons[i] = new Rectangle(0,0,0,0);
        generate(35, 35, 2, 50, 10, 10);

        save = e -> world.save(JOptionPane.showInputDialog("Map's name:"));
        open = e -> {
            JFileChooser jfc = new JFileChooser(Universe.mapPath);
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("Only .map files are accepted", "map"));
            if (jfc.showOpenDialog(MainFrame.instance) == JFileChooser.APPROVE_OPTION){
                try{
                    world = new MapRenderer(jfc.getSelectedFile().getName());
                }catch(Exception exc){}
            }
        };

        menu = e -> MainFrame.setScene(new MenuController());
    }

    public class Adaptater implements ChangeListener {

        final JSlider width, height, seed, land, moutain, wood;

        public Adaptater (JSlider width, JSlider height, JSlider seed, JSlider land, JSlider moutain, JSlider wood) {
            this.width  = width;
            this.height = height;
            this.seed   = seed;
            this.land   = land;
            this.moutain   = moutain;
            this.wood   = wood;

            width.addChangeListener(this);
            height.addChangeListener(this);
            seed.addChangeListener(this);
            land.addChangeListener(this);
            moutain.addChangeListener(this);
            wood.addChangeListener(this);
        }

        public void stateChanged (ChangeEvent e) {
            generate(width.getValue(),
                     height.getValue(),
                     seed.getValue(),
                     land.getValue(),
                     moutain.getValue(),
                     wood.getValue());
        }
    }

    public class TerrainListener implements ActionListener {

        private JButton[] lands;

        public TerrainListener (JButton[] lands) {
            this.lands = lands;
            for (int i = 0; i < lands.length; i++)
                lands[i].addActionListener(this);
        }

        public void actionPerformed (ActionEvent e) {
            for (int i = 0; i < lands.length; i++)
                if (lands[i] == e.getSource())
                    landing = i;
        }

    }

    @Override
    public void mouseMoved (MouseEvent e) {
        mouse.x = e.getX() / MainFrame.UNIT;
        mouse.y = e.getY() / MainFrame.UNIT;
    }

    public void generate (int width, int height, int seed, int l, int m, int w) {
        MapGenerator mGen = new MapGenerator(seed, 4);
        mGen.setMapWidth(width);
        mGen.setMapHeight(height);
        mGen.setLandProportion(l);
        mGen.setMountainProportion(m);
        mGen.setWoodProportion(w);
        mGen.setSeaBandSize(4);
        AbstractTerrain[][] map = new AbstractTerrain[height][width];
        TerrainEnum[][] eMap    = mGen.randMap(height, width);
        Player[] ps             = mGen.getLastPlayers();

        for (int i = 0; i < eMap.length; i++)
            for (int j = 0; j < eMap[0].length; j++)
                map[i][j] = eMap[i][j].terrain;

        AbstractUnit[][] units         = new AbstractUnit[height][width];
        AbstractBuilding[][] buildings = new AbstractBuilding[height][width];

        // save previous coordinates so
        // the camera doesn't move while generating new world

        int x = 0, y = 0;
        if (camera != null) {
            x = camera.getX();
            y = camera.getY();
        }
        world  = new MapRenderer(new Universe.Board(units, null, map, buildings, null, null, 0));
        camera = new Position.Camera(world.getDimension());
        camera.setLocation(x, y);
    }

    public EditorView makeView () {
        view = new EditorView(this);
        return view;
    }

    public void mousePressed (MouseEvent e) {
        pt = new Point(e.getX(), e.getY());
        press = true;
    }

    public void mouseReleased (MouseEvent e) {
        pt = null;
        press = false;
    }

    public void update () {
        isListening = camera.move();

        if (!isListening && press) {
            if (arrowButtons[0].contains(pt))
                camera.setDirection(Direction.RIGHT);
            else if (arrowButtons[1].contains(pt) &&
                             camera.canMove(Direction.BOTTOM))
                camera.setDirection(Direction.BOTTOM);
            else if (arrowButtons[2].contains(pt) &&
                             camera.canMove(Direction.TOP))
                camera.setDirection(Direction.TOP);
            else if (arrowButtons[3].contains(pt) &&
                             camera.canMove(Direction.LEFT))
                camera.setDirection(Direction.LEFT);
            else {
                AbstractTerrain t = null;
                switch (landing) {
                case BEACH:     t = Beach.get();    break;
                case BRIDGE:    t = Bridge.get();   break;
                case HILL:      t = Hill.get();     break;
                case LOWLAND:   t = Lowland.get();  break;
                case MOUNTAIN:  t = Mountain.get(); break;
                case RIVER:     t = River.get();    break;
                case ROAD:      t = Road.get();     break;
                case WOOD:      t = Wood.get();     break;
                case REEF:      t = Reef.get();     break;
                case SEA:       t = Sea.get();      break;
                }

                if (t != null)
                    world.setTerrain(t, new Point((int)(camera.getX() + mouse.getX()),
                                                  (int)(camera.getY() + mouse.getY())));
                TerrainRenderer.setLocations();
            }
        }

        // TODO: clean

        if (view != null) {
            if (camera != null) {
                camera.width = (view.map.getWidth() + 1) / MainFrame.UNIT;
                camera.height = (view.map.getHeight() + 1) / MainFrame.UNIT;
            }

            Rectangle rect = view.map.getBounds(null);

            int m1 = ((int)rect.getWidth()) / 2 - b/2,
                m2 = ((int)rect.getWidth()) - h - 20,
                m3 = ((int)rect.getHeight()) / 2 - b/2,
                m4 = ((int)rect.getHeight()) - h - 20;

            int[][] basePoints = new int[][]{
                {m2, m3},
                {m1, m4},
                {m1, 20},
                {20, m3}
            };

            for (int i = 0; i < 4; i++) {
                arrowButtons[i].setBounds(basePoints[i][0], basePoints[i][1], b, b);
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 3; k++)
                        arrows[i][j][k] = basePoints[i][j] + refs[i][j][k];
            }

        }

    }

}
