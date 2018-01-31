package fr.main.model;

public class Player {

  public final String name;
  public final int id;
  private static int increment_id = 0;

  public Player (String name) {
    this.name = name;
    id = ++increment_id;
  } 

}

