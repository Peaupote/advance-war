package fr.main.model;

import java.io.*;
import java.awt.Graphics;
import java.awt.Dimension;

import fr.main.view.MainFrame;
import fr.main.model.terrains.Terrain;

public class Universe {

  private static class Board implements java.io.Serializable {
    
    private Terrain[][] board;

    public Board (Terrain[][] board) {
      this.board = board;
    }
  }

  private Board map;

  public Universe (String mapPath) {
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
  }

  public void draw (Graphics g, int x, int y) {
    for (int i = y; i < y + MainFrame.WIDTH / MainFrame.UNIT; i++)
      for (int j = x; j < x + MainFrame.HEIGHT / MainFrame.UNIT; j++)
        map.board[i][j].draw(g, (j - x) * MainFrame.UNIT, (i - y) * MainFrame.UNIT);
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

  public static void save (String path, Terrain[][] map) {
    Board board = new Board (map);

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
