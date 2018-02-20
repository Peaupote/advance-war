package fr.main.model.generator;

import fr.main.model.TerrainEnum;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.naval.Sea;

import java.util.Arrays;
import java.util.Random;

import static fr.main.model.TerrainEnum.*;

public class MapGenerator {
    private long seed;
    private Random rand;

    public MapGenerator(long seed) {
        this.seed = seed;
        this.rand = new Random(seed);
    }

    public MapGenerator(int seed) {
        this((long) seed);
    }

    public void setSeed(long seed) {
        this.seed = seed;
        rand = new Random(seed);
        System.out.println("Seed set to : " + seed);

    }
    public void setSeed(int seed) {
        setSeed((long) seed);
    }
    public void resetSeed() {
        rand = new Random(seed);
        System.out.println("Rand reset to : " + seed);
    }

    public TerrainEnum[][] randMap(int x, int y) {
        int power = 4; // power * 10 = size% of cells that will serve as reference;
        int smoothness = 3;

        return randMap(x, y, power, smoothness);
    }

    // Voivonoi
    public TerrainEnum[][] randMap(int x, int y, int power, int smoothness) {
        System.out.println("Map size : " + x + "x" + y);
        if(x < 0) x = 1;
        if(y < 0) y = 1;
        if(power < 1) power = 1;
        if(power > 10) power = 10;
        if(smoothness < 0) smoothness = 0;

        resetSeed();
        int size = x * y;
        int referenceNb = size / 10 * power;
        TerrainEnum[][] map = new TerrainEnum[x][y];
        for(TerrainEnum[] line : map) Arrays.fill(line, none);


        System.out.println("Reference : " + referenceNb);

        int[] terrainPortion = new int[2];
        terrainPortion[0] = 40; // Lowland
        terrainPortion[1] = 50; // Sea
        // Somme <= 100

        int[] terrainLeft = new int[2];
        for(int i = 0; i < terrainPortion.length; i ++)
            terrainLeft[i] = referenceNb * terrainPortion[i] / 100;

        int randX, randY, count = 0;
        int[][] referencePoints = new int[referenceNb][3];

        for(int i = 0; i < terrainPortion.length; i ++) {
            System.out.println("Terrain : " + i + "\nNb : " + terrainLeft[i]);
            for(int j = 0; j < terrainLeft[i]; j ++) {
                randX = rand.nextInt(x);
                randY = rand.nextInt(y);
                if (map[randX][randY] == none) {
                    map[randX][randY] = getTerrainEnum(i);
                    referencePoints[count][0] = randX;
                    referencePoints[count][1] = randY;
                    referencePoints[count][2] = i;
                    count++;
                } else j--;
            }
        }

        for(int i = 0; i < x; i ++) {
            for(int j = 0; j < y; j++) {
                if(map[i][j] == none) {
                    map[i][j] = getTerrainEnum(referencePoints[closestPoint(i, j, referencePoints)][2]);
                }
            }
        }

        return placeBeach(refineMap(makeValidLowland(refineMap(map, smoothness)), 2));
    }

    private int distance(int x1 , int y1, int x2, int y2) {
        int x, y;

        x = x1 - x2;
        y = y1 - y2;

        if(x < 0) x = -x;
        if(y < 0) y = -y;

        return x + y;
    }

    private int closestPoint(int x, int y, int[][] points) {
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

    private TerrainEnum[][] refineMap(TerrainEnum[][] map, int it) {
        TerrainEnum[][] mapBis = new TerrainEnum[map.length][map[0].length];
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++)
                mapBis[i][j] = map[i][j];

        for(int i = 0; i < it; i ++) {
            mapBis = cellAutomaton(mapBis);
        }
        return mapBis;
    }

    private TerrainEnum[][] refineMap(TerrainEnum[][] map) {
        return refineMap(map, 1);
    }

    private TerrainEnum[][] cellAutomaton(TerrainEnum[][] map) {
        TerrainEnum[][] mapBis = new TerrainEnum[map.length][map[0].length];
        int count;

        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                count = 0;
                if(i - 1 >= 0 && map[i - 1][j] == lowland) count ++;
                if(i + 1 < map.length && map[i + 1][j] == lowland) count ++;
                if(j - 1 >= 0 && map[i][j - 1] == lowland) count ++;
                if(j + 1 < map[0].length && map[i][j + 1] == lowland) count ++;
                switch (count) {
                    case 0 : mapBis[i][j] = sea; break;
                    case 1 : mapBis[i][j] = sea; break;
                    case 2 : mapBis[i][j] = map[i][j]; break;
                    case 3 : mapBis[i][j] = lowland; break;
                    case 4 : mapBis[i][j] = lowland; break;
                }
            }
        }
        return mapBis;
    }

    private TerrainEnum[][] placeBeach(TerrainEnum[][] map) {
        int count;

        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == sea) continue;
                count = 0;
                if(i - 1 >= 0 && map[i - 1][j] == sea) count ++;
                if(i + 1 < map.length && map[i + 1][j] == sea) count ++;
                if(j - 1 >= 0 && map[i][j - 1] == sea) count ++;
                if(j + 1 < map[0].length && map[i][j + 1] == sea) count ++;

                switch (count) {
                    case 1 : map[i][j] = beach; break;
                    case 2 :
                        if(isInMap(map, i - 1, j) && map[i - 1][j] == sea
                                && isInMap(map, i + 1, j) && map[i + 1][j] == sea
                                || isInMap(map, i, j - 1) && map[i][j - 1] == sea
                                && isInMap(map, i, j + 1) && map[i][j + 1] == sea)
                            map[i][j] = bridge;
                        else map[i][j] = beach;
                        break;
                    case 3: map[i][j] = beach; break;
                    case 4: map[i][j] = reef; break;
                }
            }
        }

        return map;
    }

    private TerrainEnum[][] makeValidLowland (TerrainEnum[][] map) {
        int countH, countV;

        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == sea) continue;
                int right = terrainTypeNb(map, i, j, lowland, 0, 2),
                        left = terrainTypeNb(map, i, j, lowland, 2, 2),
                        down = terrainTypeNb(map, i, j, lowland, 1, 2),
                        up = terrainTypeNb(map, i, j, lowland, 3, 2);

//                if(left + right == 1)
//                    if(left > right && i + 1 < map.length && isValidLowland(map,i + 1, j)) map[i + 1][j] = lowland;
//                    else if(i - 1 >= 0 && isValidLowland(map, i - 1, j)) map[i - 1][j] = lowland;
//                if(up + down == 1)
//                    if(up > down && j + 1 < map[0].length && isValidLowland(map, i, j + 1)) map[i][j + 1] = lowland;
//                    else if(j - 1 >= 0 && isValidLowland(map, i, j - 1)) map[i][j - 1] = lowland;
                  if(left + right == 1 || up + down == 1) map[i][j] = sea;
            }
        }
        return map;
    }


    private int getSurroundingTerrainNb(TerrainEnum[][] map, int x, int y, TerrainEnum type, Boolean direction, int range) {
        // Direction: true = horizontal; false = vertical.
        int beginning = 0, end = 0, count = 0;

        if(direction) {
            if(x - range < 0) beginning = 0;
            else beginning = x - range;
            if(x + range >= map.length) end = map.length - 1;
            else end = x + range;
        } else {
            if(y - range < 0) beginning = 0;
            else beginning = y - range;
            if(y + range >= map[0].length) end = map[0].length - 1;
            else end = y + range;
        }

        for(int i = beginning; i <= end; i ++) {
            if(direction && i == x || !direction && i == y) continue;
            if(direction && map[i][y] == type || !direction && map[x][i] == type) count ++;
        }

        return count;
    }

    private int terrainTypeNb(TerrainEnum[][] map, int x, int y, TerrainEnum type, int direction, int range) {
        int count = 0;
        switch (direction) {
            case 0: for(int i = 1; i <= range && x + i < map.length; i ++) if(map[x + i][y] == type) count ++; else break;
                break;
            case 1: for(int i = 1; i <= range && y + i < map[0].length; i ++) if(map[x][y + i] == type) count ++; else break;
                break;
            case 2: for(int i = 1; i <= range && x - i >= 0; i ++) if(map [x - i][y] == type) count ++; else break;
                break;
            case 3: for(int i = 1; i <= range && y - i >= 0; i ++) if(map [x][y - i] == type) count ++; else break;
                break;
            default: System.out.println("Wrong argument in terrainTypeNb : direction = " + direction);
        }
        return count;
    }

    private boolean isValidLowland(TerrainEnum[][] map, int x, int y) {

        return getAdjacentTerrainNb(map, x, y, lowland) <= 2;
    }

    private int getAdjacentTerrainNb(TerrainEnum[][] map, int x , int y, TerrainEnum type) {
      int count = 0;

      if(isInMap(map, x + 1, y) && map[x + 1][y] == type) count ++;
      if(isInMap(map, x - 1, y) && map[x - 1][y] == type) count ++;
      if(isInMap(map, x, y + 1) && map[x][y + 1] == type) count ++;
      if(isInMap(map, x, y - 1) && map[x][y - 1] == type) count ++;

      return count;
    }

    private boolean isInMap(TerrainEnum[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }
}
