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

    instance = this;
    players = new PlayerIt(map.players).iterator();

    int i = 0;

    fogwar = new boolean[map.board.length][map.board[0].length];
    next();
  }

  public Dimension getDimension () {
    return new Dimension(map.board[0].length, map.board.length);
  }

  public Player getCurrentPlayer () {
    return current;
  }

  public boolean isVisible (int x, int y) {
    return fogwar[y][x];
  }

  public void next () {
    current = players.next();
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

  public static void save (String path, Unit[][] units, Terrain[][] map, Player[] ps) {
    Board board = new Board (units, ps, map);

    try {
      FileOutputStream fileOut = new FileOutputStream(path);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(board);
      out.close();
      fileOut.close();
      System.out.println("Serialized data is saved in " + path);
    } catch (IOException i) {
       i.printStackTrace();
    }
  }

  public static Universe get () {
    return instance;
  }

  // Voivonoi
  public static int[][] randMap(int x, int y) {

      //TODO: Move to new class
    int size = x * y;
    int power = 5; // power * 10 = size% of cells that will serve as reference;
    int referenceNb = size / 10 * power;
    int[][] map = new int [x][y];
    for(int[] line : map) Arrays.fill(line, -1);


    System.out.println("Reference : " + referenceNb);

    int[] terrainPortion = new int[3];
    terrainPortion[0] = 50; // Lowland
    terrainPortion[1] = 50; // Sea
    terrainPortion[2] = 0;  // Reed
                            // Somme == 100

    int[] terrainLeft = new int[3];
    for(int i = 0; i < terrainPortion.length; i ++)
      terrainLeft[i] = referenceNb * terrainPortion[i] / 100;

    Random rand = new Random();

    int randX, randY, count = 0;
    int[][] referencePoints = new int[referenceNb][3];

    for(int i = 0; i < terrainPortion.length; i ++) {
      System.out.println("Terrain : " + i + "\nNb : " + terrainLeft[i]);
      for(int j = 0; j < terrainLeft[i]; j ++) {
        randX = rand.nextInt(x);
        randY = rand.nextInt(y);
        if (map[randX][randY] == -1) {
          map[randX][randY] = i;
          referencePoints[count][0] = randX;
          referencePoints[count][1] = randY;
          referencePoints[count][2] = i;
          count++;
        } else j--;
      }
    }

    for(int i = 0; i < x; i ++) {
      for(int j = 0; j < y; j++) {
        if(map[i][j] == -1) {
          map[i][j] = referencePoints[closestPoint(i, j, referencePoints)][2];
        }
      }
    }

    return refineMap(map, 4);
  }

  private static int distance(int x1 , int y1, int x2, int y2) {
    int x, y;

    x = x1 - x2;
    y = y1 - y2;

    if(x < 0) x = -x;
    if(y < 0) y = -y;

    return x + y;
  }

  private static int closestPoint(int x, int y, int[][] points) {
    int closest = distance(x, y, points[0][0], points[0][1]);
    int dist;
    int closestPoint = 0;

    for(int i = 0; i < points.length; i ++) {
      dist = distance(x, y, points[i][0], points[i][1]);
      if(dist < closest) {
        closest = dist;
        closestPoint = i;
      }
    }
    return closestPoint;
  }

  private static int[][] refineMap(int[][] map, int it) {
    int[][] mapBis = new int[map.length][map[0].length];
    for(int i = 0; i < map.length; i ++)
      for(int j = 0; j < map[0].length; j ++)
        mapBis[i][j] = map[i][j];

    for(int i = 0; i < it; i ++) {
      mapBis = cellAutomaton(mapBis);
    }
    return mapBis;
  }

  private static int[][] refineMap(int[][] map) {
    return refineMap(map, 1);
  }

  private static int[][] cellAutomaton(int[][] map) {
    int[][] mapBis = new int[map.length][map[0].length];
    int count;

    for(int i = 0; i < map.length; i ++) {
      for(int j = 0; j < map[0].length; j ++) {
        count = 0;
        if(i - 1 >= 0 && map[i - 1][j] == 0) count ++;
        if(i + 1 < map.length && map[i + 1][j] == 0) count ++;
        if(j - 1 >= 0 && map[i][j - 1] == 0) count ++;
        if(j + 1 < map[0].length && map[i][j + 1] == 0) count ++;
        switch (count) {
          case 0 : mapBis[i][j] = 1; break;
          case 1 : mapBis[i][j] = 1; break;
          case 2 : mapBis[i][j] = map[i][j]; break;
          case 3 : mapBis[i][j] = 0; break;
          case 4 : mapBis[i][j] = 0; break;
        }
      }
    }
    return mapBis;
  }

}
