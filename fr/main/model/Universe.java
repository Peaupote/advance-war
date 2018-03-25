package fr.main.model;

import java.io.*;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.buildings.*;
import fr.main.model.PlayerIt.Cycle;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HideableUnit;
import fr.main.view.render.units.naval.*;
import fr.main.view.render.units.air.*;
import fr.main.view.render.units.land.*;
import fr.main.model.units.naval.*;
import fr.main.model.units.air.*;
import fr.main.model.units.land.*;
import fr.main.model.terrains.AbstractBuildable;
import fr.main.model.commanders.FakeCommander;
import fr.main.model.Weather;

public class Universe {

    private static Universe instance;
    public static final String mapPath="maps/";
    public static boolean save=false;
    private Weather weather;

    static{
        File maps=new File(mapPath);
        if (!maps.exists() && !maps.isDirectory() && !maps.mkdir())
            System.out.println("Impossible to save.");
        else save = true;
    }

    protected static class Board implements Serializable {

        public AbstractTerrain[][] board;
        public Player[] players;
        public AbstractUnit[][] units;
        public AbstractBuilding[][] buildings;
        private final boolean fog;

        public Board (AbstractUnit[][] units, Player[] ps, AbstractTerrain[][] board, AbstractBuilding[][] buildings) {
            this.fog       = true;
            this.board     = board;
            this.units     = units;
            this.players   = ps;
            this.buildings = buildings;
        }
    }

    protected Board map;
    protected final Cycle players;
    protected Player current;
    protected boolean[][] fogwar;
    private Dimension size;

    public Universe (String mapName) {
        map = null;

        if (save){
            try {
                FileInputStream fileIn = new FileInputStream(mapPath+mapName);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                map = (Board) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Board class not found");
                e.printStackTrace();
            }
        }

        instance = this;
        weather  = Weather.FOGGY;
        fogwar   = new boolean[map.board.length][map.board[0].length];
        players  = new PlayerIt(map.players).cycle();
        next();

        new Dock(map.players[0], new Point(6,10));
        new Airport(map.players[1], new Point(6,9));
        new Airport(map.players[0], new Point(7,9));
        new Barrack(null, new Point(9,5));
        new Headquarter(map.players[0], new Point(12,5));
        new City(null, new Point(20,5));

        new Lander(map.players[0], new Point(0,0));
        new Lander(map.players[1], new Point(1,1));
        new Lander(map.players[0], new Point(3,17));
        new Infantry(map.players[0], new Point(10,5));
        new Infantry(map.players[1], new Point(10,6));
        new Fighter(map.players[0], new Point(10,10));

        map.players[0].addFunds(100000);
        map.players[1].addFunds(100000);

        if (getBuilding(6,10) != null)
            ((Dock)getBuilding(6,10)).create(Battleship.class);
        if (getBuilding(7,9) != null)
            ((Airport)getBuilding(7,9)).create(Stealth.class);
        if (getBuilding(6,9) != null)
            ((Airport)getBuilding(6,9)).create(Fighter.class);

    }

    public Dimension getDimension () {
        if (size == null) size = new Dimension(map.board[0].length, map.board.length);
        return size;
    }

    public Player getCurrentPlayer () {
        return current;
    }

    public boolean isVisible (int x, int y) {
        return isValidPosition(x,y) && fogwar[y][x];
    }

    public boolean isVisible (Point pt) {
        return isVisible(pt.x, pt.y);
    }

    public synchronized void next () {
        if (current != null)
            current.turnEnds();

        if (!players.hasNext())
            System.exit(0);

        do
            current = players.next(); 
        while (current.hasLost());

        current.turnBegins();

        if (players.isFirst(current)){
            Weather w = Weather.random(map.fog);
            if (w.fog && !weather.fog)
                for (int i = 0; i < map.board.length; i++)
                    for (int j = 0; j < map.board[0].length; j++)
                        fogwar[i][j] = true;
            weather = w;
        }

        updateVision ();
    }

    public void updateVision () {
        if (weather.fog){
            for (int i = 0; i < map.board.length; i++)
                for (int j = 0; j < map.board[0].length; j++)
                    fogwar[i][j] = false;
            current.renderVision(fogwar);
        }
    }

    public final AbstractTerrain getTerrain (int x, int y) {
        return isValidPosition(x,y) ? map.board[y][x] : null;
    }

    public final AbstractTerrain getTerrain (Point pt) {
        return getTerrain(pt.x, pt.y);
    }

    public final AbstractBuilding getBuilding(int x, int y) {
        return isValidPosition(x,y) ? map.buildings[y][x] : null;
    }

    public final AbstractBuilding getBuilding (Point pt) {
        return getBuilding(pt.x, pt.y);
    }

    public final AbstractUnit getUnit(int x, int y) {
        return isValidPosition(x,y) ? map.units[y][x] : null;
    }

    public final AbstractUnit getUnit (Point pt) {
        return getUnit(pt.x, pt.y);
    }

    public final boolean isValidPosition(int x, int y){
        return y>=0 && x>=0 && y<map.units.length && x<map.units[0].length;
    } 

    public final boolean isValidPosition(Point p){
        return isValidPosition(p.x,p.y);
    }

    public final boolean setUnit(int x, int y, AbstractUnit u) {
        if (isValidPosition(x,y)){
            map.units[y][x] = u;
            return true;
        }

        return false;
    }

    public final boolean setBuilding(int x, int y, AbstractBuilding b){
        if (isValidPosition(x, y)){
            map.buildings[y][x] = b;
            return true;
        }
        
        return false;
    }

    public String toString () {
        String ret = "";
        for (int i = 0; i < map.board.length; i++) {
            for (int j = 0; j < map.board[0].length; j++)
                ret += map.board[i][j] + " ";
            ret += "\n";
        }
        return ret;
    }

    public boolean isVisibleOpponentUnit(int x, int y){
        AbstractUnit unit = getUnit(x, y);
        if (isVisible(x,y) && unit != null && unit.getPlayer() != getCurrentPlayer()){
            if (unit instanceof HideableUnit && ((HideableUnit)unit).hidden()){
                for (Direction d : Direction.cardinalDirections())
                    if (getUnit(x + d.x, y + d.y) != null && getUnit(x + d.x, y + d.y).getPlayer() == getCurrentPlayer())
                        return true;
                return false;
            }
            return true;
        }
        return false;
    }

    public static void save (String mapName, AbstractUnit[][] units, AbstractTerrain[][] map, Player[] ps, AbstractBuilding[][] buildings) {
        if (!Universe.save){
            System.out.println("Impossible to save.");
            return;
        }

        Board board = new Board (units, ps, map, buildings);

        try {
            FileOutputStream fileOut = new FileOutputStream(mapPath+mapName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(board);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + mapPath+mapName);
        } catch (IOException i) {
             i.printStackTrace();
        }
    }

    public static Universe get () {
        return instance;
    }

    public Weather getWeather(){
        return weather;
    }

    public int getMapHeight(){
        return map.board.length;
    }

    public int getMapWidth(){
        return map.board[0].length;
    }

}
