package fr.main.model;

import java.util.Arrays;
import java.util.Random;

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

    public int[][] randMap(int x, int y) {
        int power = 4; // power * 10 = size% of cells that will serve as reference;
        int smootness = 3;

        return randMap(x, y, power, smootness);
    }

    // Voivonoi
    public int[][] randMap(int x, int y, int power, int smoothness) {
        if(x < 0) x = 1;
        if(y < 0) y = 1;
        if(power < 1) power = 1;
        if(power > 10) power = 10;
        if(smoothness < 0) smoothness = 0;

        resetSeed();
        int size = x * y;
        int referenceNb = size / 10 * power;
        int[][] map = new int [x][y];
        for(int[] line : map) Arrays.fill(line, -1);


        System.out.println("Reference : " + referenceNb);

        int[] terrainPortion = new int[3];
        terrainPortion[0] = 50; // Lowland
        terrainPortion[1] = 50; // Sea
        terrainPortion[2] = 0;  // Reef
        // Somme == 100

        int[] terrainLeft = new int[3];
        for(int i = 0; i < terrainPortion.length; i ++)
            terrainLeft[i] = referenceNb * terrainPortion[i] / 100;

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

        return refineMap(map, smoothness);
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

    private int[][] refineMap(int[][] map, int it) {
        int[][] mapBis = new int[map.length][map[0].length];
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++)
                mapBis[i][j] = map[i][j];

        for(int i = 0; i < it; i ++) {
            mapBis = cellAutomaton(mapBis);
        }
        return mapBis;
    }

    private int[][] refineMap(int[][] map) {
        return refineMap(map, 1);
    }

    private int[][] cellAutomaton(int[][] map) {
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
