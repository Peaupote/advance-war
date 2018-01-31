package fr.main.model;

import java.io.*;
import java.awt.Dimension;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class Universe {

  protected static class Board implements Serializable {

    public Terrain[][] board;
    public Unit[][] units;

    public Board (Unit[][] units, Terrain[][] board) {
      this.board = board;
      this.units = units;
    }
  }

  protected Board map;
  protected final PlayerIt players;
  protected Player current;

  public Universe (String mapPath, Player[] ps) {
    map = null;

    try {
      FileInputStream fileIn = new FileInputStream(mapPath);
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

    players = new PlayerIt(ps);

    int i = 0;
    for (Player p: ps) {
      for (Unit unit: map.units[i])
        unit.setPlayer(p);
      i++;
    }

    current = players.iterator().next();

  }

  public Dimension getDimension () {
    return new Dimension(map.board[0].length, map.board.length);
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

  public static void save (String path, Unit[][] units, Terrain[][] map) {
    Board board = new Board (units, map);

    try {
      FileOutputStream fileOut =
      new FileOutputStream(path);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(board);
      out.close();
      fileOut.close();
      System.out.println("Serialized data is saved in " + path);
    } catch (IOException i) {
       i.printStackTrace();
    }
  }

}
