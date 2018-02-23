package fr.main;

import java.awt.EventQueue;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.main.model.generator.MapGenerator;
import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.Unit;
import fr.main.view.render.units.LanderRenderer;
import fr.main.view.MainFrame;

/**
 * Classe qui lance le projet
 */
public class App {

  public static void main (String[] args) {
    int s = 50;
    Terrain[][] map = new Terrain[s][s];
    TerrainEnum[][] eMap = new MapGenerator(2).randMap(s, s);

    for (int i = 0; i < eMap.length; i++)
      for (int j = 0; j < eMap[0].length; j++)
        map[i][j] = eMap[i][j].terrain;


    Player[] players = new Player[]{
      new Player("P1"), new Player("P2")
    };

    AbstractUnit[][] units = new Unit[s][s];
    units[0][0] = new LanderRenderer(new Point(0,0));
    units[1][1] = new LanderRenderer(new Point(1,1));
    units[3][6] = new LanderRenderer(new Point(6,3));

    players[0].add(units[0][0]);
    players[0].add(units[3][6]);
    players[1].add(units[1][1]);

    Universe.save("maptest.map", units, map, players);

    //Universe world = new Universe("maptest.map");
    //System.out.println(world);


    
    EventQueue.invokeLater(() -> {
		try {
			new MainFrame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	});
  }
}
