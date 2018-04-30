package fr.main.model;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import fr.main.model.PlayerIt.Cycle;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.City;
import fr.main.model.buildings.GenericBuilding;
import fr.main.model.buildings.Headquarter;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.players.AIPlayer;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.HideableUnit;


/**
 * Represents the universe of a game (board, ...).
 */
public class Universe {

    /**
     * The current instance of the universe.
     */
    private static Universe instance;

    /**
     * The path to the directory where maps are saved.
     */
    public static final String mapPath = "./maps/";

    /**
     * boolean set to true if and only if the game can be saved.
     */
    public static boolean save = false;


    static{
        File maps = new File(mapPath);
        if (!maps.exists() && !maps.isDirectory() && !maps.mkdir())
            System.out.println("Impossible to save.");
        else save = true;
    }

    /**
     * Represents a board (terrains, buildings, units, players).
     */
    public static class Board implements Serializable {

        /**
         * Add Board UID.
         */
        private static final long serialVersionUID = -8731719580943357396L;
        public AbstractTerrain[][] board;
        public Player[] players;
        public AbstractUnit[][] units;
        public AbstractBuilding[][] buildings;
        private final boolean fog;
        protected Weather weather;
        protected Player current;

        /**
         * the number of the day.
         */
        protected int day;

        public Board (AbstractUnit[][] units, Player[] ps, AbstractTerrain[][] board, AbstractBuilding[][] buildings, Weather weather, Player current, int day) {
            this.fog       = true;
            this.board     = board;
            this.units     = units;
            this.players   = ps;
            this.buildings = buildings;
            this.weather   = weather;
            this.current   = current;
            this.day       = day;
        }

        public Board(AbstractUnit[][] units, Player[] ps, AbstractTerrain[][] board, AbstractBuilding[][] buildings){
            this(units, ps, board, buildings, Weather.random(true), null, 0);
        }

        public Board setPlayers(Player[] ps){
            Board board = new Board(units, ps, this.board, buildings, weather, current, day);

            for (int i = 0; i < this.players.length; i++){
                if (current == this.players[i]){
                    if (i >= ps.length){
                        board.current = null;
                        day ++;
                    }else board.current = ps[i];
                }
                Headquarter hq = null;
                for (OwnableBuilding bu : this.players[i].buildingList())
                    if (bu instanceof Headquarter){
                        hq = (Headquarter)bu;
                        break;
                    }

                Player tmp     = i >= 0 && i < ps.length ? ps[i] : null;
                if (hq == null)
                    System.out.println("No HQ for " + tmp.name + "from " + players[i].name);
                else{
                    hq.changeOwner(tmp);
                    if (tmp == null)
                        board.buildings[hq.getY()][hq.getX()] = new City(null, new Point (hq.getX(), hq.getY()));
                } 
            }

            return board;
        }
    }

    protected final Board map;
    /**
     * An Iterator<Player> which is a cycle (when we're on the last player, the next is the first)
     */
    protected final Cycle players;
    /**
     * The vision of the map (the tiles that the current player can see are true)
     */
    protected boolean[][] fogwar;
    /**
     * The size of the map
     */
    private Dimension size;

    public Universe (AbstractUnit[][] units, AbstractTerrain[][] map, Player[] ps, AbstractBuilding[][] buildings){
        this (new Board(units, ps, map, buildings));
    }

    public Universe (String mapName, Player[] ps) {
        this (Universe.restaure(mapName).setPlayers(ps));
    }

    public Universe (Board b){
        map = b;

        instance = this;
        fogwar   = new boolean[map.board.length][map.board[0].length];

        // TODO: make something cleaner
        if (map.players != null){
            players  = new PlayerIt(map.players).cycle();
            if (map.current == null) next();
            else{
                players.setCurrent(map.current);
                updateVision();
            } 
        }else players = null;
    }

    public static Board restaure(String mapName){
        if (save)
            try (FileInputStream fileIn = new FileInputStream(mapPath + mapName);
                    ObjectInputStream in = new ObjectInputStream(fileIn)) {
                return (Board) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Board class not found");
                e.printStackTrace();
            }
        return null;
    }

    /**
     * @param p is the player that looses
     * When a player looses, if there is only one player left then the game is done
     */
    public void playerLoose(Player p){
        if (!players.hasNext())
            System.exit(0);
    }

    /**
     * @return the dimension of the map
     */
    public Dimension getDimension () {
        if (size == null) size = new Dimension(map.board[0].length, map.board.length);
        return size;
    }

    /**
     * @return the player currently playing
     */
    public Player getCurrentPlayer () {
        return map.current;
    }

    /**
     * @return true if the point given in parameter can be seen by the current player
     */
    public boolean isVisible (int x, int y) {
        return isValidPosition(x,y) && fogwar[y][x];
    }

    /**
     * @param pt is the point considered
     * @return true if the point given in parameter can be seen by the current player
     */
    public boolean isVisible (Point pt) {
        return isVisible(pt.x, pt.y);
    }

    /**
     * @return an array containing the players
     */
    public Player[] playerList(){
        return Arrays.copyOf(map.players,map.players.length);
    }

    /**
     * @return the day number
     */
    public int getDay(){
        return map.day;
    }

    /**
     * Goes to the next player (that hasn't lose yet)
     */
    public synchronized void next () {
        if (map.current != null)
            map.current.turnEnds();

        do
            map.current = players.next();
        while (map.current.hasLost());

        map.current.turnBegins();

        if (players.isFirst(map.current)){
            map.day ++;
            Weather w = Weather.random(map.fog);
            if (w.fog && !map.weather.fog)
                for (int i = 0; i < map.board.length; i++)
                    for (int j = 0; j < map.board[0].length; j++)
                        fogwar[i][j] = true;
            map.weather = w;
        }

        updateVision ();
    }

    /**
     * update the vision of the current player
     */
    public void updateVision () {
        if (map.weather.fog){
            for (int i = 0; i < map.board.length; i++)
                for (int j = 0; j < map.board[0].length; j++)
                    fogwar[i][j] = false;
            map.current.renderVision(fogwar);
        }
    }

    /**
     * activate the small power of the current player
     */
    public void smallPower(){
        getCurrentPlayer().getCommander().activate(false);
    }

    /**
     * activate the big power of the current player
     */
    public void bigPower(){
        getCurrentPlayer().getCommander().activate(true);
    }

    /**
     * @return the terrain at the specified location
     */
    public final AbstractTerrain getTerrain (int x, int y) {
        return isValidPosition(x,y) ? map.board[y][x] : null;
    }

    /**
     * @return the terrain at the specified location
     */
    public final AbstractTerrain getTerrain (Point pt) {
        return getTerrain(pt.x, pt.y);
    }

    public final void setTerrain (AbstractTerrain t, int x, int y) {
      if (isValidPosition(x, y)) {
        map.board[y][x] = t;
      }
    }

    public final void setTerrain (AbstractTerrain t, Point pt) {
      setTerrain(t, pt.x, pt.y);
    }

    /**
     * @return the building at the specified location
     */
    public final AbstractBuilding getBuilding(int x, int y) {
        return isValidPosition(x,y) ? map.buildings[y][x] : null;
    }

    /**
     * @return the building at the specified location
     */
    public final AbstractBuilding getBuilding (Point pt) {
        return getBuilding(pt.x, pt.y);
    }

    /**
     * @return the unit at the specified location
     */
    public final AbstractUnit getUnit(int x, int y) {
        return isValidPosition(x,y) ? map.units[y][x] : null;
    }

    /**
     * @return the unit at the specified location
     */
    public final AbstractUnit getUnit (Point pt) {
        return getUnit(pt.x, pt.y);
    }

    /**
     * @return true if and only if the specified location is valid
     */
    public final boolean isValidPosition(int x, int y){
        return y>=0 && x>=0 && y<map.units.length && x<map.units[0].length;
    }

    /**
     * @return true if and only if the specified location is valid
     */
    public final boolean isValidPosition(Point p){
        return isValidPosition(p.x,p.y);
    }

    /**
     * @param x is the horizontal coordinate of the tile in which we want to place the unit
     * @param y is the vertical coordinate of the tile in which we want to place the unit
     * @param u is the unit we place on the map
     * @return true if and only if the position is valid
     */
    public boolean setUnit(int x, int y, AbstractUnit u) {
        if (isValidPosition(x,y)){
            map.units[y][x] = u;
            return true;
        }

        return false;
    }

    /**
     * @param x is the horizontal coordinate of the tile in which we want to place the building
     * @param y is the vertical coordinate of the tile in which we want to place the building
     * @param b is the building we place on the map
     * @return true if and only if the position is valid
     */
    public boolean setBuilding(int x, int y, AbstractBuilding b){
        if (isValidPosition(x, y)){

            // the AI reads the whole map at the beginning to search for buildings that will be considered as objectives,
            // if a building is changed, the AI is warned
            if (map.buildings[y][x] != null)
                for (Player p : map.players)
                    if (p instanceof AIPlayer)
                        ((AIPlayer)p).changeBuilding(map.buildings[y][x], b);

            map.buildings[y][x] = b;
            return true;
        }

        return false;
    }

    public void setBuildings(AbstractBuilding[] bs) {
		for (AbstractBuilding b : bs) {
			if (b instanceof GenericBuilding) continue;
			if (isValidPosition(b.getX(), b.getY()))
				map.buildings[b.getY()][b.getX()] = b;
			else
				System.err.println(b.toString() + " not initialized in Universe.");
		}
	}

	public void setBuildings(AbstractBuilding[][] bss) {
    	for(int i = 0; i < bss.length; i ++)
    		for(int j = 0; j < bss[0].length; j ++)
    			if(!(bss[i][j] instanceof GenericBuilding))
    				if(!setBuilding(i, j, bss[i][j]))
    					System.err.println(bss[i][j].toString() + " not initialized in Universe.");
	}

    @Override
    public String toString () {
        String ret = "";
        for (int i = 0; i < map.board.length; i++) {
            for (int j = 0; j < map.board[0].length; j++)
                ret += map.board[i][j] + " ";
            ret += "\n";
        }
        return ret;
    }

    /**
     * @param x is the horizontal coordinate of the tile considered
     * @param y is the vertical coordinate of the tile considered
     * @return true if and only if there is an opponent unit at the location specified and the unit is visible by the current player
     */
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

    public boolean isVisibleOpponentUnit (Point pt){
        return isVisibleOpponentUnit(pt.x, pt.y);
    }

    /**
     * @param mapName is the name of the save file
     * @param units is the units board
     * @param map is the terrain board
     * @param ps is the array of players
     * @param buildings is the buildings board
     * save the board described in parameter
     */
    public static void save (String mapName, AbstractUnit[][] units, AbstractTerrain[][] map, Player[] ps, AbstractBuilding[][] buildings) {
        Universe.save(mapName, new Board (units, ps, map, buildings));
    }

    /**
     * @param mapName is the name of the save file
     * @param board is the board to save
     */
    public static void save(String mapName, Board board){
        if (!Universe.save){
            System.out.println("Impossible to save.");
            return;
        }

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

    public void save(String mapName){
        Universe.save(mapName + ".map", map);
    }

    /**
     * Saves the game with a default name with the format year-month-day-hour-minute-second
     */
    public void save(){
        Universe.save((new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(new Date(System.currentTimeMillis())) + ".map", map);
    }

    /**
     * @return the current instance of the universe
     */
    public static Universe get () {
        return instance;
    }

    /**
     * @return the weather
     */
    public Weather getWeather(){
        return map.weather;
    }

    /**
     * @return the height of the map
     */
    public int getMapHeight(){
        return map.board.length;
    }

    /**
     * @return the width of the map
     */
    public int getMapWidth(){
        return map.board[0].length;
    }

    public int getNumberOfPlayers() {
    	return this.map.players.length;
	}

}
