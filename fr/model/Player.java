package model;

public class Player {

  public final String name;
  public final int id;
  private static int increment_id = 0;
  
  int imgx;
  int imgy;
  public int selectx = 2;
  public int selecty = 2;

  public Player (String name) {
    this.name = name;
    id = ++increment_id;
  } 

}

