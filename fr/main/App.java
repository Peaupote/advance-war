package fr.main;

import java.io.IOException;
import fr.main.model.generator.MapGenerator;
import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.commanders.FakeCommander;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;

/**
 * Classe qui lance le projet
 */
public class App {

  public static void main (String[] args) throws Exception {
    String s = args == null || args.length == 0 ? "undefined" : args[0];
    switch (s) {
      case "play": play();break;
      case "save": save();break;
      default: 
        save();
        play();
        break;
    }
  }

  public static void play () throws Exception{
	  new MainFrame();
  }

  public static void save () {
    int s = 50;
    AbstractTerrain[][] map = new AbstractTerrain[s][s];
    TerrainEnum[][] eMap = new MapGenerator(2).randMap(s, s);

    for (int i = 0; i < eMap.length; i++)
      for (int j = 0; j < eMap[0].length; j++)
		  map[i][j] = eMap[i][j].terrain;


    Player[] players = new Player[]{
      new Player("P1"), new Player("P2")
    };

    for (Player p: players) new FakeCommander(p);

    AbstractUnit[][] units = new AbstractUnit[s][s];
    AbstractBuilding[][] buildings = new AbstractBuilding[s][s];

    Universe.save("maptest.map", units, map, players, buildings);
  }
}
