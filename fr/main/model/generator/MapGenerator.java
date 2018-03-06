package fr.main.model.generator;

import fr.main.model.TerrainEnum;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.TerrainLocation;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.ReefRenderer;
import fr.main.view.render.terrains.naval.RiverRenderer;
import fr.main.view.render.terrains.naval.SeaRenderer;

import java.util.Arrays;
import java.util.Random;

import static fr.main.model.TerrainEnum.*;

public class MapGenerator {
    private long seed;
    private Random rand;

    public MapGenerator() {
    	this(1);
	}

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
        int smoothness = 2;

        return randMap(x, y, power, smoothness);
    }
	/* x = vertical, y = horizontal */
    // Voivonoi
    public TerrainEnum[][] randMap(int x, int y, int power, int smoothness) {
        System.out.println("Map size : " + x + "x" + y);
        if(x < 0) x = 1;
        if(y < 0) y = 1;
        if(power < 1) power = 1;
        if(power > 10) power = 10;
        if(smoothness < 0) smoothness = 6;

        resetSeed();
        int size = x * y;
        int referenceNb = size / 10 * power;
        TerrainEnum[][] map = new TerrainEnum[x][y];
        for(TerrainEnum[] line : map) Arrays.fill(line, none);


        System.out.println("Reference : " + referenceNb);

        int[] terrainPortion = new int[2];
        terrainPortion[0] = 20; // Lowland
        terrainPortion[1] = 30; // Sea
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

        return placeWood(placeMountainsHills(placeBeach(surroundBySea(refineMap(map, smoothness), 4))));
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
                    case 1 : if(rand.nextInt( 10) < 3) map[i][j] = beach; break;
                    case 2 :
                        if(isInMap(map, i - 1, j) && map[i - 1][j] == sea
                                && isInMap(map, i + 1, j) && map[i + 1][j] == sea
                                || isInMap(map, i, j - 1) && map[i][j - 1] == sea
                                && isInMap(map, i, j + 1) && map[i][j + 1] == sea)
                            map[i][j] = bridge;
                        else if(rand.nextInt( 10) < 8) map[i][j] = beach;
                        break;
                    case 3: if(rand.nextInt( 10) < 6) map[i][j] = beach; break;
                    case 4: map[i][j] = reef; break;
                }
            }
        }

        for(int i = 0; i < map.length; i ++)
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == beach
                        && (getAdjacentTerrainNb(map, i, j, beach) == 0
                        || getAdjacentTerrainNb(map, i, j, bridge) != 0))
                    map[i][j] = lowland;
                if(getAdjacentTerrainNb(map, i, j, beach) == 1 && getAdjacentTerrainNb(map, i, j, lowland) == 2)
                    map[i][j] = lowland;

            }

        return map;
    }

    private TerrainEnum[][] placeMountainsHills(TerrainEnum[][] map) {
        int mountains = 0;
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == lowland && getAdjacentTerrainNb(map, i, j, bridge) == 0) {
                    mountains = getAdjacentTerrainNb(map, i, j, mountain);
                    if(mountains == 0 && rand.nextInt(15) < 1) map[i][j] = mountain;
                    else if(mountains > 0 && rand.nextInt(15) < 4) map[i][j] = mountain;
                    else if(mountains == 0 && rand.nextInt(12) < 2) map[i][j] = hill;
                    else if((mountains > 0 || getAdjacentTerrainNb(map, i, j, hill) > 0)
                            && rand.nextInt(12) < 3) map[i][j] = hill;
                }
            }
        return map;
    }

    private TerrainEnum[][] placeWood(TerrainEnum[][] map) {
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == lowland || map[i][j] == hill)
                    if(getAdjacentTerrainNb(map, i, j, wood) == 0)
                        map[i][j] = (rand.nextInt(12) < 2) ? wood : map[i][j];
                    else map[i][j] = (rand.nextInt(10) < 2) ? wood: map[i][j];
            }
        return map;
    }

    private TerrainEnum[][] makeValidLowland (TerrainEnum[][] map) {
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
        int[] terrainNb = new int[4];
        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == sea) continue;
                terrainNb[0] = terrainTypeNb(map, i, j, lowland, 0, 1);
                terrainNb[1] = terrainTypeNb(map, i, j, lowland, 1, 1);
                terrainNb[2]= terrainTypeNb(map, i, j, lowland, 2, 1);
                terrainNb[3]= terrainTypeNb(map, i, j, lowland, 3, 1);

//                System.out.println("(" + i + ":" + j + ")" + "\n0 : " + terrainNb[0] + "\n1 : " + terrainNb[1] + "\n2 : " + terrainNb[2] + "\n3 : " + terrainNb[3]);
//                System.out.println(terrainNb[0] + terrainNb[1] + terrainNb[2] + terrainNb[3]);

                switch(terrainNb[0] + terrainNb[1] + terrainNb[2] + terrainNb[3]) {
                    case 0 : map[i][j] = sea; break;
                    case 1 : map[i][j] = sea; break;
                    case 2 : validLowland2(map, terrainNb, i, j); break;
                    case 3 : validLowland3(map, terrainNb, i, j); break;
                }

            }
        }
//        TerrainEnum[][] mapBis = map.clone();
//
//        boolean[] block = new boolean[4];
//
//        for(int i = 0; i < map.length; i ++) {
//            for(int j = 0; j < map[0].length; j ++) {
//                if(isSurrounded(map, lowland, i, j)) continue;
//                for(int h = 0; h < 4; h ++)
//                    block[h] = hasSide3x3Block(map, lowland, h, i, j);
//
//                if(block[0] && block[1] && isInMap(map, i + 2, j + 2)) mapBis[i + 2][j + 2] = lowland;
//                else if(block[2] && block[1] && isInMap(map, i - 2, j + 2)) mapBis[i - 2][j + 2] = lowland;
//                else if(block[2] && block[3] && isInMap(map, i - 2, j - 2)) mapBis[i - 2][j - 2] = lowland;
//                else if(block[0] && block[3] && isInMap(map, i + 2, j - 2)) mapBis[i + 2][j - 2] = lowland;
//                else map[i][j] = sea;
//            }
//        }

        return map;
    }

    private void validLowland2 (TerrainEnum[][] map, int[] adj, int x, int y) {
        if(adj[0] == adj[2]) return;
        TerrainEnum[][] out = map.clone();
        if(adj[0] + adj[1] == 2 && map[x + 1][y + 1] == sea
                || adj[1] + adj[2] == 2 && map[x - 1][y + 1] == sea
                || adj[2] + adj[3] == 2 && map[x - 1][y - 1] == sea
                || adj[3] + adj[0] == 2 && map[x + 1][y - 1] == sea)
            map[x][y] = sea;
    }

    private void validLowland3 (TerrainEnum[][] map, int[] adj, int x, int y) {
        if(adj[0] == 0) {
            if(isInMap(map,x - 1, y - 1)) map[x - 1][y - 1] = lowland;
            if(isInMap(map,x - 1, y + 1)) map[x - 1][y + 1] = lowland;
        } else if(adj[1] == 0) {
            if(isInMap(map,x - 1, y - 1)) map[x - 1][y - 1] = lowland;
            if(isInMap(map,x + 1, y - 1)) map[x + 1][y - 1] = lowland;
        } else if(adj[2] == 0) {
            if(isInMap(map,x + 1, y - 1)) map[x + 1][y - 1] = lowland;
            if(isInMap(map,x + 1, y + 1)) map[x + 1][y + 1] = lowland;
        } else if(adj[3] == 0) {
            if(isInMap(map,x - 1, y + 1)) map[x - 1][y + 1] = lowland;
            if(isInMap(map,x + 1, y + 1)) map[x + 1][y + 1] = lowland;
        }
    }

    private void placeRivers (TerrainEnum[][] map) {
    	/*** To use BEFORE setting any land-type Terrain other than Lowland and Beach. ***/
//    	int nSea, nLowland, nBridge, nBeach, nRiver;
    	TerrainEnum[] land = {lowland, bridge, beach}, naval = {sea, river};

    	for(int i = 0; i < map.length; i ++)
    		for(int j = 0; j < map[0].length; j ++)
    			if(map[i][j] == sea) {
    				if(isSandwiched(map, i, j, land)
							&& getAdjacentTerrainNb(map, i, j, lowland)
							+ getAdjacentTerrainNb(map, i, j, beach) < 3)
    					map[i][j] = river; //continue;
//    				nSea = getAdjacentTerrainNb(map, i , j, sea);
//    				nLowland = getAdjacentTerrainNb(map, i, j, lowland);
//					nBridge = getAdjacentTerrainNb(map, i, j, bridge);
//					nRiver = getAdjacentTerrainNb(map, i, j , river);
//					nBeach = getAdjacentTerrainNb(map, i, j, beach);
				}
		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++)
				if(map[i][j] == sea && getAdjacentTerrainNb(map, i, j, river) >= 2)
					map[i][j] = river;
	}

    private boolean isSurrounded(TerrainEnum[][] map, TerrainEnum type, int x, int y) {
        for(int i = x - 1; i <= x + 1; i ++)
            for(int j = y - 1; j <= y + 1; j ++)
                if(isInMap(map, i, j) && map[i][j] != type)
                    return false;
        return true;
    }

//    private boolean hasSide3x3Block(TerrainEnum[][] map, TerrainEnum type, int direction, int x, int y) {
//        switch (direction) {
//            case 0: return isBlock(map, type, x, y - 1, 3);
//            case 1: return isBlock(map, type, x - 1, y, 3);
//            case 2: return isBlock(map, type, x - 2, y - 1, 3);
//            case 3: return isBlock(map, type, x - 1, y - 2, 3);
//            default: System.out.println("Wrong argument in 'hasSide3x3Block' : direction = " + direction);
//            return false;
//        }
//    }
//
//    private boolean isBlock(TerrainEnum[][] map, TerrainEnum type, int x, int y, int size) {
//        for(int i = x; i <= size; i ++)
//            for(int j = y; j <= size; j ++)
//                if(!isInMap(map, i, j) || map[i][j] != type)
//                    return false;
//        return true;
//    }


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

    private boolean isSandwiched(TerrainEnum map[][], int x, int y, TerrainEnum type) {
    	return isSandwiched(map, x, y, type, true) || isSandwiched(map, x, y, type, false);
	}

	private boolean isSandwiched(TerrainEnum map[][], int x, int y, TerrainEnum type[]) {
		return isSandwiched(map, x, y, type, true) || isSandwiched(map, x, y, type, false);
	}

	private boolean isSandwiched (TerrainEnum map[][], int x, int y, TerrainEnum type, boolean horizontal) {
		TerrainEnum[] ts = {type};
    	return isSandwiched(map, x, y, ts, horizontal);
	}

    private boolean isSandwiched (TerrainEnum map[][], int x, int y, TerrainEnum type[], boolean horizontal) {
    	return (!horizontal && isInMap(map, x - 1, y) && isInMap(map, x + 1, y) && hasMatch(type, map[x - 1][y]) && hasMatch(type, map[x + 1][y]))
				|| (horizontal && isInMap(map, x, y - 1) && isInMap(map, x, y + 1) && hasMatch(type, map[x][y - 1]) && hasMatch(type, map[x][y + 1])	);
	}

	private boolean hasMatch(TerrainEnum ts[], TerrainEnum type) {
		for (TerrainEnum t : ts)
			if(t == type) return true;
		return false;
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
        return getAdjacentTerrainNb(map, x, y, lowland) > 2;
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

    private TerrainEnum[][] surroundBySea(TerrainEnum[][] map, int size) {
        for(int i = 0; i < map.length; i ++)
            for (int j = 0; j < map[0].length; j++)
                if(i <= size || map.length - i <= size || j <= size || map[0].length - j <= size)
                    map[i][j] = sea;
        return map;
    }

    private TerrainEnum[][] placeRoads(TerrainEnum[][] map, int nb) {
        while(nb > 0)
            for(int i = 0; i < map.length; i ++)
                for (int j = 0; j < map[0].length; j++) {

                }

        return map;
    }

    private TerrainLocation[][] getLocations(TerrainEnum[][] map) {
    	TerrainLocation[][] out = new TerrainLocation[map.length][map[0].length];
		//TODO finish this function.
    	for(int i = 0; i < map.length; i ++)
    		for(int j = 0; j < map[0].length; j ++)
    			switch (map[i][j]) {
					case lowland:
						if(isInMap(map, i, j - 1)
								&& (map[i][j - 1] == mountain || map[i][j - 1] == hill || map[i][j - 1] == wood))
							out[i][j] = LowlandRenderer.LowlandLocation.SHADOW;
						else out[i][j] = LowlandRenderer.LowlandLocation.NORMAL;
						break;
					case sea: 		out[i][j] = SeaRenderer.SeaLocation.NORMAL; 			break; 	//TODO
					case wood:		out[i][j] = WoodRenderer.WoodLocation.NORMAL; 			break;
					case reef:		out[i][j] = ReefRenderer.ReefLocation.NORMAL; 			break;
					case road:		out[i][j] = RoadRenderer.RoadLocation.CENTER; 			break; 	//TODO
					case mountain: 	out[i][j] = MountainRenderer.MountainLocation.NORMAL; 	break;
					case hill: 		out[i][j] = HillRenderer.HillLocation.NORMAL; 			break;
					case beach: 	out[i][j] = BeachRenderer.BeachLocation.LEFT; 			break; 	//TODO
					case river: 	out[i][j] = RiverRenderer.RiverLocation.CENTER; 		break; 	//TODO
					case bridge:
						if(isInMap(map, i - 1, j) && map[i - 1][j] == sea)
							out[i][j] = BridgeRenderer.BridgeLocation.HORIZONTAL;
						else out[i][j] = BridgeRenderer.BridgeLocation.VERTICAL;
						break;
				}
		return out;
	}


	public TerrainLocation[][] getLocations(Terrain[][] map) {
    	TerrainEnum[][] mapBis = new TerrainEnum[map.length][map[0].length];
		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++)
				mapBis[i][j] = TerrainEnum.getTerrainEnum(map[i][j]);
		return getLocations(mapBis);
	}
}
