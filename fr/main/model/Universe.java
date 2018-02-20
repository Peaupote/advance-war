package fr.main.model;

import java.io.*;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class Universe {

  private static Universe instance;
  public static final String mapPath="maps/";
  public static boolean save=false;

  static{
      File maps=new File(mapPath);
      if (!maps.exists() && !maps.isDirectory() && !maps.mkdir())
        System.out.println("Impossible to save.");
      else
        save=true;
  }

  protected static class Board implements Serializable {

    public Terrain[][] board;
    public Player[] players;
    public Unit[][] units;

    public Board (Unit[][] units, Player[] ps, Terrain[][] board) {
      this.board = board;
      this.units = units;
      this.players = ps;
    }
  }

  protected Board map;
  protected final Iterator<Player> players;
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
    players = new PlayerIt(map.players).iterator();

    int i = 0;

    fogwar = new boolean[map.board.length][map.board[0].length];
    next();
  }

  public Dimension getDimension () {
    if (size == null) size = new Dimension(map.board[0].length, map.board.length);
    return size;
  }

  public Player getCurrentPlayer () {
    return current;
  }

  public boolean isVisible (int x, int y) {
    return fogwar[y][x];
  }

  public void next () {
    current = players.next();
    updateVision ();
  }

  public void updateVision () {
    for (int i = 0; i < map.board.length; i++)
      for (int j = 0; j < map.board[0].length; j++)
        fogwar[i][j] = false;

    for (Unit u: current)
      u.renderVision(fogwar);
  }

  public final Terrain getTerrain (int x, int y) {
    return map.board[y][x];
  }

  public final Unit getUnit(int x, int y) {
    if (y>=0 && x>=0 && y<map.units.length && x<map.units[0].length)
      return map.units[y][x];
    return null;
  }

  public final void setUnit(int x, int y, Unit u) {
    map.units[y][x] = u;
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

  public static void save (String mapName, Unit[][] units, Terrain[][] map, Player[] ps) {
    if (!Universe.save){
      System.out.println("Impossible to save.");
      return;
    }

    Board board = new Board (units, ps, map);

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

}
