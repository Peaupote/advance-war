package fr.main;

import java.io.File;

import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.generator.MapGenerator;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.network.Client;
import fr.main.network.Server;
import fr.main.view.MainFrame;
import fr.main.view.controllers.GameController;

/**
 * Classe qui lance le projet
 */
public class App {

    public static void main (String[] args) throws Exception {
        String s = args == null || args.length == 0 ? "undefined" : args[0];
        switch (s) {
            case "play"   : play(); break;
            case "save"   : save(); break;
            case "server" : new Server(8080); break;
            case "client" : new Client("localhost", 8080); break;
            case "debug"  :
                if ((new File(Universe.mapPath + "debug.map")).exists()){
                    new MainFrame();
                    MainFrame.setScene(new GameController("debug.map"));
                }
                else
                    System.out.println("The debug map doesn't exists. Please name a save debug.map");
                break;
            default:
                save();
                play();
        }
    }

  public static void play () throws Exception{
	  new MainFrame();
  }

  /**
   * Procedural generation of map in maps/maptest.map
   */
  public static void save () {
    int s = 30;

    MapGenerator mGen = new MapGenerator(100, 4);
    mGen.setStarterBarrack(true);
    mGen.setStarterAirport(true);
    mGen.setStarterDock(true);
    mGen.setCityRingNb(2);
    mGen.setBarracksNb(3);
    mGen.setDocksNb(2);
    mGen.setAirportNb(1);
    mGen.setPlaceSilo(true);

    mGen.setSeaBandSize(4);
    AbstractTerrain[][] map = new AbstractTerrain[s][s];
    TerrainEnum[][] eMap = mGen.randMap(s, s);
    Player[] ps = mGen.getLastPlayers();

    for (int i = 0; i < eMap.length; i++)
      for (int j = 0; j < eMap[0].length; j++)
		  map[i][j] = eMap[i][j].terrain;

    AbstractUnit[][] units = new AbstractUnit[s][s];
    AbstractBuilding[][] buildings = mGen.getLastBuildingLayout();

    Universe.save("maptest.map", units, map, ps, buildings);
  }
}
