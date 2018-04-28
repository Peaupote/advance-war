package fr.main.model.generator;

import fr.main.model.TerrainEnum;
import fr.main.model.buildings.*;
import fr.main.model.players.Player;
import fr.main.model.terrains.Terrain;
import sun.awt.image.ImageWatched;


import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static fr.main.model.TerrainEnum.*;

public class MapGenerator {
    private long seed;
    private Random rand;
    private Player[] players;
    private TerrainEnum[][] lastMap;
    private TerrainEnum[][] currentMap;
    private AbstractBuilding[][] lastBuildingLayout;
    private AbstractBuilding[][] currentBuildingLayout;
    private Player[] lastPlayers;

    private int[][] lastHQCoordinates;
    private int[][] currentHQCoordinates;

    private boolean starterBarrack, starterAirport, starterDock;

    // TODO put all mapGen parameters

	private int seaProportion, landProportion, mountainProportion, woodProportion,
			smoothness, power, seaBandSize,
			mapHeight, mapWidth;

	/**
	 * Generates a MapGenerator of seed 1 and for 2 players.
	 */
	public MapGenerator() {
    	this(1, 2);
	}

	/**
	 * Generates a MapGenerator with a LONG seed.
	 * @param seed
	 */
    public MapGenerator(long seed, int playerNb) {
    	setSeed(seed);
    	this.rand = new Random(seed);
        setPlayers(playerNb);
        setSeaProportion(50);
        setLandProportion(60);
        this.starterAirport = false;
        this.starterBarrack = true;
    }

	/**
	 * Generates a map generator with an INTEGER seed.
	 * @param seed
	 */
	public MapGenerator(int seed, int playerNb) {
        this((long) seed, playerNb);
    }

    public void setSeed(long seed) {
		if(seed < 0) seed = -seed;
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

	public void setPlayers(int playerNb) {
		if(playerNb < 2) playerNb = 2;
		if(playerNb > 4) playerNb = 4;
		players = new Player[playerNb];
		for(int i = 1; i <= playerNb; i ++)
			players[i - 1] = new Player("p" + i);
	}

	public Player[] getPlayers() {
		return players;
	}

	public Player[] getLastPlayers() {
		return lastPlayers;
	}

	public TerrainEnum[][] getLastMap() {
		return lastMap;
	}

	public void setSmoothness(int s) {
		this.smoothness = makeInBound(0, 10, s);
	}

	public int getPower() {
		return power;
	}

	public AbstractBuilding[][] getLastBuildingLayout() {
		return lastBuildingLayout;
	}

	public int getLandProportion() {
		return landProportion;
	}

	public int getSeaProportion() {
		return seaProportion;
	}

	public int getSmoothness() {
		return smoothness;
	}

	public long getSeed() {
		return seed;
	}

	public Random getRand() {
		return rand;
	}

	public int getSeaBandSize() {
		return seaBandSize;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMountainProportion() {
		return mountainProportion;
	}

	public int getWoodProportion() {
		return woodProportion;
	}

	public int[][] getCurrentHQCoordinates() {
		if(currentHQCoordinates == null)
			setCurrentHQCoordinates(currentBuildingLayout);
		return currentHQCoordinates;
	}

	public int[][] getLastHQCoordinates() {
		return lastHQCoordinates;
	}

	public boolean isStarterAirport() {
		return starterAirport;
	}

	public boolean isStarterBarrack() {
		return starterBarrack;
	}

	public AbstractBuilding[][] getCurrentBuildingLayout() {
		return currentBuildingLayout;
	}

	public TerrainEnum[][] getCurrentMap() {
		return currentMap;
	}

	public void setMountainProportion(int mountainProportion) {
		this.mountainProportion = mountainProportion; // TODO
	}

	public void setWoodProportion(int woodProportion) {
		this.woodProportion = woodProportion; // TODO
	}

	public boolean isStarterDock() {
		return starterDock;
	}

	public void setStarterAirport(boolean starterAirport) {
		this.starterAirport = starterAirport;
	}

	public void setStarterBarrack(boolean starterBarrack) {
		this.starterBarrack = starterBarrack;
	}

	public void setStarterDock(boolean starterDock) {
		this.starterDock = starterDock;
	}

	public void setSeaBandSize(int seaBandSize) {
		if(seaBandSize < 0) seaBandSize = 0;
		else if (seaBandSize > mapHeight / 2) seaBandSize = mapHeight / 2 + 1;
		else if (seaBandSize > mapWidth / 2) seaBandSize = mapWidth / 2 + 1;
		this.seaBandSize = seaBandSize;
	}

	public void setMapHeight(int mapHeight) {
		this.mapHeight = makeInBound(10, 60, mapHeight);
	}

	public void setMapWidth(int mapWidth) {
		this.mapWidth = makeInBound(10, 60, mapWidth);
	}

	public void setPower(int p) {
		this.power = makeInBound(0, 10, p);
	}

	public void setLandProportion(int landProportion) {
		this.landProportion = makeInBound(0, 100 - (seaProportion - (seaProportion > 20 ? 20 : 0)), landProportion);
	}

	public void setSeaProportion(int seaProportion) {
		this.seaProportion = makeInBound(0, 100 - (landProportion - (landProportion > 20 ? 20 : 0)), seaProportion);
	}

	private void setCurrentHQCoordinates(AbstractBuilding[][] layout) {
		LinkedList<Headquarter> hqs = new LinkedList<>();

		for(int i = 0; i < layout.length; i ++)
			for(int j = 0; j < layout[0].length; j ++)
				if(layout[i][j] instanceof Headquarter)
					hqs.add((Headquarter) layout[i][j]);

		currentHQCoordinates = new int[hqs.size()][2];

		for(int i = 0; i < hqs.size(); i ++) {
			currentHQCoordinates[i][0] = hqs.get(i).getX();
			currentHQCoordinates[i][1] = hqs.get(i).getY();
		}
	}

	public void resetCurrentHQCorrdinates() {
		currentHQCoordinates = null;
	}

	private int makeInBound(int min, int max, int i) {
		if(i < min) i = min;
		else if (i > max) i = max;
		return i;
	}

	public TerrainEnum[][] randMap() {
		return randMap(mapHeight, mapWidth);
	}

	/**
	 * Creates a random map.
	 * @param x vertical size.
	 * @param y horizontal size.
	 * @return
	 */
    public TerrainEnum[][] randMap(int x, int y) {
		setMapHeight(x);
		setMapWidth(y);
        System.out.println("Map size : " + x + "x" + y);
        if(x < 0) x = 1;
        if(y < 0) y = 1;
        if(power < 1) power = 1;
        if(power > 10) power = 10;
        if(smoothness < 0) smoothness = 6;

        resetSeed();
        int size = x * y;
        int referenceNb = size / 10 * power;

        currentMap = new TerrainEnum[x][y];
        for(TerrainEnum[] line : currentMap) Arrays.fill(line, none);

		currentBuildingLayout = new AbstractBuilding[x][y];
		for(int i = 0; i < x; i ++)
			for(int j = 0; j < y; j ++)
				currentBuildingLayout[i][j] = new GenericBuilding(i, j);

		System.out.println("Reference : " + referenceNb);

        int[] terrainPortion = new int[2];
        terrainPortion[0] = 20; // Lowland
        terrainPortion[1] = 30; // Sea
        // Somme <= 100

        int[] terrainLeft = new int[2];
        for(int i = 0; i < terrainPortion.length; i ++)
            terrainLeft[i] = referenceNb * terrainPortion[i] / 100;

        /*
        	Places the HQs
         */

		currentBuildingLayout = placeHeadQuarters(currentBuildingLayout);
		currentBuildingLayout = placeStarterBuildings(currentBuildingLayout);

		setImmuableTerrain(currentBuildingLayout, currentMap);

        /*
        	Places the reference points for the Voronoi algorithm.
         */
		int[][] hqs = getCurrentHQCoordinates();

		int randX, randY, count = hqs.length;
		int[][] referencePoints = new int[referenceNb + hqs.length][3];

		for(int i = 0; i < hqs.length; i ++) {
			referencePoints[i][0] = hqs[i][0];
			referencePoints[i][1] = hqs[i][1];
			referencePoints[i][2] = 0;
		}

		for(int i = 0; i < terrainPortion.length; i ++) {
            System.out.println("Terrain : " + i + "\nNb : " + terrainLeft[i]);
            for(int j = 0; j < terrainLeft[i]; j ++) {
                randX = rand.nextInt(x);
                randY = rand.nextInt(y);
                if (currentMap[randX][randY] == none) {
                    currentMap[randX][randY] = getTerrainEnum(i);
                    referencePoints[count][0] = randX;
                    referencePoints[count][1] = randY;
                    referencePoints[count][2] = i;
                    count++;
                } else j--;
            }
        }

        for(int i = 0; i < x; i ++) {
            for(int j = 0; j < y; j++) {
                if(currentMap[i][j] == none) {
                    currentMap[i][j] = getTerrainEnum(referencePoints[closestPoint(i, j, referencePoints)][2]);
                }
            }
        }

        /*
        	Cleaning the buldings arrays.
         */

        clean(currentBuildingLayout);

        refineMap(currentMap, smoothness);
        currentMap = surroundBySea(currentMap, seaBandSize);
        placeBeach(currentMap);
        currentMap = placeRivers(currentMap);
        placeMountainsHills(currentMap);
        placeWood(currentMap);
        clean(currentMap);


		lastBuildingLayout = currentBuildingLayout;
        currentBuildingLayout = null;
        lastMap = currentMap;
        currentMap = null;
        lastPlayers = players;

        return lastMap;
    }

    private void printCoorArray(int[][] array) {
    	String str = "";
    	for(int[] l : array) {
    		str = "(";
    		for(int i : l)
    			str += i + ",";
			System.out.println(str + ")");
		}
	}

    private AbstractBuilding[][] placeStarterBuildings(AbstractBuilding[][] layout) {
		int[][] hqs = getCurrentHQCoordinates();

		if(hqs == null)  {
			System.err.println("Current HQ coordinates not set");
			return layout;
		}

		int[][] sqrCoor;
		int randNb;

		for(int[] coor : hqs) {
			sqrCoor = getSquare(layout.length, layout[0].length, coor[0], coor[1]);
			printCoorArray(sqrCoor);
			if(sqrCoor.length == 0) {
				System.out.println("No valid cell for stater building.");
				continue;
			}
			if(starterBarrack) {
				while(true) {
					randNb = rand.nextInt(sqrCoor.length);
					if (sqrCoor[randNb] == null) {
						System.out.println("sqr[" + randNb + "] null.");
						continue;
					}
					if (!(layout[sqrCoor[randNb][0]][sqrCoor[randNb][1]] instanceof GenericBuilding))
						continue;

					layout[sqrCoor[randNb][0]][sqrCoor[randNb][1]] =
							new Barrack(
									((Headquarter) layout[coor[0]][coor[1]]).getOwner(),
									new Point(sqrCoor[randNb][0], sqrCoor[randNb][1]));
					break;
				}
			}
			if(starterBarrack && starterBarrack && sqrCoor.length < 2) {
				System.out.println("No place for starter Airport");
			}
			if (starterAirport)
				while (true) {
					randNb = rand.nextInt(sqrCoor.length);
					if (sqrCoor[randNb] == null) {
						System.out.println("sqr[" + randNb + "] null.");
						continue;
					}
					if (!(layout[sqrCoor[randNb][0]][sqrCoor[randNb][1]] instanceof GenericBuilding))
						continue;

					layout[sqrCoor[randNb][0]][sqrCoor[randNb][1]] =
							new Airport(
									((Headquarter) layout[coor[0]][coor[1]]).getOwner(),
									new Point(sqrCoor[randNb][0], sqrCoor[randNb][1]));
					break;
				}
			if((starterBarrack || starterBarrack) && starterDock && sqrCoor.length < 2
					|| starterBarrack && starterBarrack && starterDock && sqrCoor.length < 3) {
				System.out.println("No place for starter Dock");
			}
			if (starterDock) {
				// TODO find nearest sea tile.
			}

		}


		/*
			TODO : test placeStarterBuilings and maybe add more ? EDIT: problem - function not placing the buildings.
		 */

		return layout;
	}

    private AbstractBuilding[][] placeHeadQuarters(AbstractBuilding[][] layout) {
    	int x = layout.length,
				y = layout[0].length;
    	int randNb = rand.nextInt(3);
    	int realX = x - seaBandSize - 1 - randNb,
				realY = y - seaBandSize - 1 - randNb;
		boolean useWidth = x < y;

		System.out.println("realX : " + realX + "\nrealY :" + realY);

		AbstractBuilding[] rect = getRectangleFromSide(layout, seaBandSize + 1 + randNb);

		LinkedList<Integer> hqs = new LinkedList<>();

		int size = 0;

		switch (players.length) {
			case 2 :
				hqs.add(useWidth ? rand.nextInt() % realY : rand.nextInt() % realX + realY - 1);
				hqs.add((hqs.get(0) + realX + realY - 1) % rect.length);
				size = 2;
				break;
			case 3 :
				if(useWidth)
					if(rand.nextInt() % 2 == 0) {
						hqs.add(realY / 2);
						hqs.add(realY + realX - 1);
						hqs.add(realX + 2 * realY - 2);
					} else {
						hqs.add(0);
						hqs.add(realY);
						hqs.add(realY / 2 + realY + realX - 1);
					}
				else
					if(rand.nextInt() % 2 == 0) {
						hqs.add(realX / 2);
						hqs.add(realX + realY - 1);
						hqs.add(realY + 2 * realX - 2);
					} else {
						hqs.add(0);
						hqs.add(realX);
						hqs.add(realX / 2 + realX + realY - 1);
					}
				size = 3;
				break;
			case 4 : {
				LinkedList<Integer> l = new LinkedList<>();
				l.add(0);
				l.add(realY);
				l.add(realY + realX - 1);
				l.add(realY * 2 + realX - 1);
				int pop = 0;
				for (int i = 3; i >= 0; i--) {
					if(i > 0) pop = abs(rand.nextInt(i));
					else pop = 0;
					hqs.add(l.get(pop));
					if (l.size() <= pop) hqs.remove(hqs.size() - 1);
					else l.remove(pop);
				}
				size = 4;
				break;
			}
			default:
				System.out.println("Problem here");
				System.exit(20);
		}

		currentHQCoordinates = new int[hqs.size()][2];

		System.out.println("Headquarters nearly placed.");

		for (int i = 0; i < hqs.size(); i ++) {
			layout[rect[hqs.get(i)].getX()][rect[hqs.get(i)].getY()] = new Headquarter(players[i], new Point(rect[hqs.get(i)].getX(), rect[hqs.get(i)].getY()));
			currentHQCoordinates[i][0] = rect[hqs.get(i)].getX();
			currentHQCoordinates[i][1] = rect[hqs.get(i)].getY();
		}

		System.out.println("HQs placed !");
		return layout;
	}

	private AbstractBuilding[] getRectangleFromSide(AbstractBuilding[][] layout, int distanceFromSide) {
		int lw = layout[0].length;
		int lh = layout.length;

    	int w = 2 * (lw - 2 * distanceFromSide);
    	int h = 2 * (lh - 2 * distanceFromSide - 2);

    	if(h < 0 || w < 0) {
    		System.out.println("height or width not valid.");
    		return new AbstractBuilding[0];
		}

		AbstractBuilding[] rectangle = new AbstractBuilding[w + h];
    	int counter = 0;

    	for(int j = distanceFromSide; j < lw - distanceFromSide; j ++) {
			rectangle[counter] = layout[distanceFromSide][j];
			counter++;
		}
		for(int i = distanceFromSide + 1; i < lh - distanceFromSide - 1; i ++) {
    		rectangle[counter] = layout[i][lw - distanceFromSide - 1];
    		counter ++;
		}
		for(int i = lw - distanceFromSide - 1; i >= distanceFromSide; i --) {
    		rectangle[counter] = layout[i][lh - distanceFromSide - 1];
    		counter ++;
		}
		for(int j = lh - distanceFromSide - 2; j > distanceFromSide + 1; j --) {
    		rectangle[counter] = layout[lw - distanceFromSide - 1][j];
    		counter ++;
		}

		return rectangle;
	}

	private AbstractBuilding[] getSquare(AbstractBuilding[][] layout, int x0, int y0) {
		return getSquare(layout, x0, y0, 1);
	}

	private AbstractBuilding[] getSquare(AbstractBuilding[][] layout, int x0, int y0, int dist) {
    	return getRectangle(layout, x0, y0, dist, dist);
	}

	private AbstractBuilding[] getRectangle(AbstractBuilding[][] layout, int x0, int y0, int distH, int distW) {
    	if(layout == null) return new AbstractBuilding[0];
    	return coordinatesToAbstractBuilding(getRectangle(layout.length, layout[0].length, x0, y0, distH, distW), layout);
	}

	private int[][] getSquare(int height, int width, int x0, int y0) {
    	return getSquare(height, width, x0, y0, 1);
	}

	private int[][] getSquare(int height, int width, int x0, int y0, int dist) {
    	return getRectangle(height, width, x0, y0, dist, dist);
	}

	/**
	 * @param height of map/layout.
	 * @param width of map/layout.
	 * @param x0 of center of rectangle.
	 * @param y0 of center of rectangle.
	 * @param distH distance form (x0,y0) Horizontaly .
	 * @param distW	distance form (x0,y0) Verticaly.
	 * @return an array of coordinates from the side of a rectangle.
	 */
	private int[][] getRectangle(int height, int width, int x0, int y0, int distH, int distW) {
		int 	top = x0 - distH,
				bottom = x0 + distH,
				left = y0 - distW,
				right = y0 + distW;

		LinkedList<int[]> list = new LinkedList<>();
		int[] item;

		for(int i = left; i <= right; i ++)
			if(isInRect(top, i, height, width)) {
				item = new int[2];
				item[0] = top;
				item[1] = i;
				list.add(item);
			}

		for(int i = top + 1; i <= bottom; i ++)
			if(isInRect(i, right, height, width)) {
				item = new int[2];
				item[0] = i;
				item[1] = right;
				list.add(item);
			}

		for(int i = right - 1; i >= left; i --)
			if(isInRect(bottom, i, height, width)) {
				item = new int[2];
				item[0] = bottom;
				item[1] = i;
				list.add(item);
			}

		for(int i = bottom - 1; i > top; i --)
			if(isInRect(i, left, height, width)) {
				item = new int[2];
				item[0] = i;
				item[1] = left;
				list.add(item);
			}

		int[][] out = new int[list.size()][2];
		int count = 0;

		for(int[] it : list) {
			out[count][0] = list.get(count)[0];
			out[count][1] = list.get(count)[1];
			count ++;
		}

		return out;
	}

	private int[][] getDiamond (int height, int width, int x0, int y0, int distFromCenter) {
		if(distFromCenter < 0) throw new IllegalArgumentException();
		int[][] out;

		if(distFromCenter == 0) {
			out = new int[1][2];
			out[0][0] = x0;
			out[0][1] = y0;
			return out;
		}

		int[][] last = getDiamond(height, width, x0, y0, distFromCenter - 1);

		LinkedList<int[]> coors = new LinkedList<>();
		int[] item;

		for(int i = 0; i < last.length; i ++) {
			item = new int[2];
			if (last[i][0] <= x0 && isInRect(last[i][0] - 1, last[i][1], height, width)) {
				item[0] = last[i][0] - 1;
				item[1] = last[i][1];
				coors.add(item);
			}
			if (last[i][0] >= x0 && isInRect(last[i][0] + 1, last[i][1], height, width)) {
				item[0] = last[i][0] + 1;
				item[1] = last[i][1];
				coors.add(item);
			}
			if (last[i][0] == x0 && last[i][1] >= y0 && isInRect(x0, last[i][1] + 1, height, width)) {
				item[0] = x0;
				item[1] = last[i][1] + 1;
				coors.add(item);
			}
			if (last[i][0] == x0 && last[i][1] <= y0 && isInRect(x0, last[i][1] - 1, height, width)) {
				item[0] = x0;
				item[1] = last[i][1] - 1;
				coors.add(item);
			}
		}

		out = new int[coors.size()][2];

		for(int i = 0; i < coors.size(); i ++) {
			out[i][0] = coors.get(i)[0];
			out[i][1] = coors.get(i)[1];
		}

		return out;
	}

	private AbstractBuilding[] getCircle(AbstractBuilding[][] layout, int x0, int y0, int radius) {
		if(layout == null) return new AbstractBuilding[0];
		return coordinatesToAbstractBuilding(getCircle(layout.length, layout[0].length, x0, y0, radius), layout);
	}

	private int[][] getCircle(int heigh, int width, int x0, int y0, int radius) {
		int x = radius-1;
		int y = 0;
		int dx = 1;
		int dy = 1;
		int err = dx - (radius << 1);

		int size = 0;

		// Calculating the final size of the array returned.
		while (x >= y) {
			size += 8;

			if (err <= 0) {
				y++;
				err += dy;
				dy += 2;
			}

			if (err > 0) {
				x--;
				dx += 2;
				err += dx - (radius << 1);
			}
		}

		// Now we get all the coordinates on the circle.
		int[][] IList = new int[size][2];

		x = radius-1;
		y = 0;
		dx = 1;
		dy = 1;
		err = dx - (radius << 1);

		int count = 0;

		while (x >= y) {
			IList[count][0] = x0 + x;
			IList[count][1] = y0 + y;
			count ++;

			if (err <= 0) {
				y++;
				err += dy;
				dy += 2;
			}

			if (err > 0) {
				x--;
				dx += 2;
				err += dx - (radius << 1);
			}
		}

		// Mirror first Octant.
		for(int i = count - 1; i >= 0; i --) {
			IList[count][0] = IList[i][1];
			IList[count][1] = IList[i][0];
			count ++;
		}

		// Mirror first Quarter.
		for(int i = count - 1; i >= 0; i --) {
			IList[count][0] = -IList[i][0];
			IList[count][1] = IList[i][1];
			count ++;
		}

		// Mirror first Half.
		for(int i = count - 1; i >= 0; i --) {
			IList[count][0] = IList[i][0];
			IList[count][0] = -IList[i][1];
			count ++;
		}

		return IList;
	}

	private AbstractBuilding[] coordinatesToAbstractBuilding(int[][] coors, AbstractBuilding[][] layout) {
    	int count = 0;

		for(int i = 0; i < coors.length; i ++)
			if(isInLayout(layout, coors[i][0], coors[i][1]))
				count ++;

		AbstractBuilding[] out = new AbstractBuilding[count];

		count = 0;
		for(int i = 0; i < coors.length; i ++)
    		if(isInLayout(layout, coors[i][0], coors[i][1])) {
				out[count] = layout[coors[i][0]][coors[i][1]];
				count++;
			}
//    		else throw new NullPointerException();

    	return out;
	}

	private int[][] findNearestTerrainEnum(TerrainEnum[][] map, int i, int j, TerrainEnum[] types) {
		int[][] dmnd;
		int[][] out;
		int size = 1;
		LinkedList<int[]> found = new LinkedList<>();

		label:
		while (true) {
			dmnd = getDiamond(map.length, map[0].length, i, j, size);
			if(dmnd.length == 0)
				return new int[0][0];
			size++;
			for(int[] l : dmnd) {
				if(hasMatch(types, map[l[0]][l[1]]))
					break label;
			}
		}

		for(int[] l : dmnd)
			if(hasMatch(types, map[l[0]][l[1]]))
				found.add(l);

		out = new int[found.size()][2];

		for(int k = 0; k < found.size(); k ++) {
			out[k][0] = found.get(k)[0];
			out[k][1] = found.get(k)[1];
		}

		return out;
	}

	private TerrainEnum[][] setImmuableTerrain(AbstractBuilding[][] layout, TerrainEnum[][] map) {
    	for(int i = 0; i < layout.length; i ++)
    		for(int j = 0; j < layout[0].length; j ++) {
				if(layout[i][j] instanceof Headquarter)
					fillSquareFromCenter(map, i, j, mLowland);
				else if(!(layout[i][j] instanceof GenericBuilding))
					setCell(map, mLowland, i, j);

				// TODO : Add other cases if necessary.
			}
		return map;
	}

	private TerrainEnum[][] fillSquareFromCenter(TerrainEnum[][] map, int x, int y, TerrainEnum filler, int distanceFromCenter) {
    	if(isInMap(map, x, y) && distanceFromCenter > 0)
    		for(int i = x - distanceFromCenter; i <= x + distanceFromCenter; i ++)
    			for(int j = y - distanceFromCenter; j <= x + distanceFromCenter; j ++)
    				setCell(map, filler, i, j);
    	return map;
	}

	private TerrainEnum[][] fillSquareFromCenter(TerrainEnum[][] map, int x, int y, TerrainEnum filler) {
    	return fillSquareFromCenter(map, x, y, filler, 1);
	}


	private TerrainEnum[][] resetImmuableTerrain(TerrainEnum[][] map) {
    	for(TerrainEnum[] line : map)
    		for(TerrainEnum t : line)
    			if(t == mLowland)
    				t = lowland;
    	return map;
	}

	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return distance between (x1, y1) and (x2, y2).
	 */
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

	/**
	 * @param map
	 * @param it
	 * @return map refined by cellular automaton.
	 */
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

    @SuppressWarnings("unused")
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
                    case 3: if(rand.nextInt(10) < 6) map[i][j] = beach; break;
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
    	// TODO affine
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

//    @SuppressWarnings("unused")
	private TerrainEnum[][] makeValidLowland (TerrainEnum[][] map) {
        for(int i = 0; i < map.length; i ++) {
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == sea) continue;
                int right = terrainTypeNb(map, i, j, lowland, 0, 2),
                        left = terrainTypeNb(map, i, j, lowland, 2, 2),
                        down = terrainTypeNb(map, i, j, lowland, 1, 2),
                        up = terrainTypeNb(map, i, j, lowland, 3, 2);
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

                switch(terrainNb[0] + terrainNb[1] + terrainNb[2] + terrainNb[3]) {
                    case 0 : map[i][j] = sea; break;
                    case 1 : map[i][j] = sea; break;
                    case 2 : validLowland2(map, terrainNb, i, j); break;
                    case 3 : validLowland3(map, terrainNb, i, j); break;
                }

            }
        }

        return map;
    }

    private TerrainEnum[][] clean(TerrainEnum[][] map, int it) {
    	resetImmuableTerrain(map);
    	for(int i = 0; i < it; i ++)
    		map = clean(map);
    	return map;
	}

	private AbstractBuilding[][] clean(AbstractBuilding[][] layout) {
    	for(int i = 0; i < layout.length; i++)
    		for(int j = 0; j < layout[0].length; j ++) {
    			if(layout[i][j] instanceof GenericBuilding) {
    				if(layout[i][j] instanceof Headquarter) System.out.println("PROBLEM.");
					layout[i][j] = null;
				}
			}

		return layout;
	}

    private TerrainEnum[][] clean (TerrainEnum[][] map) {
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++) {
				switch (map[i][j]) {
					case beach:
						TerrainEnum[] goodTerrains = {wood, lowland, mLowland, mountain, hill};
						if (getAdjacentTerrainNb(map, i, j, sea) >= 3 && getAdjacentTerrainNb(map, i, j, goodTerrains) == 0)
							map[i][j] = sea;
						else if (getAdjacentTerrainNb(map, i, j, beach) < 3)
							map[i][j] = lowland;
						break;
					case sea:
						if(getAdjacentTerrainNb(map, i, j, sea) == 1)
							map[i][j] = river;
						break;
						// TODO : put every cleaning procedure here.
				}
			}

		map = placeRivers(map);

		return map;
	}

    private void validLowland2 (TerrainEnum[][] map, int[] adj, int x, int y) {
        if(adj[0] == adj[2]) return;
        @SuppressWarnings("unused")
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

    private TerrainEnum[][] placeRivers (TerrainEnum[][] map) {
		TerrainEnum[] land = {lowland, bridge, mountain, hill, wood}, naval = {sea, river};

    	for(int i = 0; i < map.length; i ++)
    		for(int j = 0; j < map[0].length; j ++)
    			if(map[i][j] == sea) {
    				if(isSandwiched(map, i, j, land)
							&& getAdjacentTerrainNb(map, i, j, lowland)
							+ getAdjacentTerrainNb(map, i, j, beach) < 3)
    					map[i][j] = river; //continue;
					else if(getAdjacentTerrainNb(map, i, j, sea) + getAdjacentTerrainNb(map, i, j, river) <= 2 && isSandwiched(map, i, j , naval))
						map[i][j] = river;
					else if(getAdjacentTerrainNb(map, i, j, sea) == 0)
						map[i][j] = river;
				}
		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++)
				if(map[i][j] == sea && getAdjacentTerrainNb(map, i, j, river) >= 2)
					map[i][j] = river;
    	return map;
	}

	private boolean isSurrounded(TerrainEnum[][] map, TerrainEnum type, int x, int y) {
        for(int i = x - 1; i <= x + 1; i ++)
            for(int j = y - 1; j <= y + 1; j ++)
                if(isInMap(map, i, j) && map[i][j] != type)
                    return false;
        return true;
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

	public static TerrainEnum[] getSurroundingTerrain(TerrainEnum[][] map, int x, int y) {
    	TerrainEnum[] out = new TerrainEnum[8];
    	Arrays.fill(out, none);

		if(isInMap(map, x - 1, y)) 			out[0] = map[x - 1][y];
		if(isInMap(map, x - 1, y + 1)) 		out[1] = map[x - 1][y + 1];
		if(isInMap(map, x, y + 1)) 			out[2] = map[x][y + 1];
		if(isInMap(map, x + 1, y + 1)) 		out[3] = map[x + 1][y + 1];
		if(isInMap(map, x + 1, y))			out[4] = map[x + 1][y];
		if(isInMap(map, x + 1, y - 1)) 		out[5] = map[x + 1][y - 1];
		if(isInMap(map, x, y - 1)) 			out[6] = map[x][y - 1];
		if(isInMap(map, x - 1, y - 1)) 		out[7] = map[x - 1][y - 1];

		return out;
	}

    public static boolean isSandwiched(TerrainEnum map[][], int x, int y, TerrainEnum type) {
    	return isSandwiched(map, x, y, type, true) || isSandwiched(map, x, y, type, false);
	}

	public static boolean isSandwiched(TerrainEnum map[][], int x, int y, TerrainEnum type[]) {
		return isSandwiched(map, x, y, type, true) || isSandwiched(map, x, y, type, false);
	}

	public static boolean isSandwiched (TerrainEnum map[][], int x, int y, TerrainEnum type, boolean horizontal) {
		TerrainEnum[] ts = {type};
    	return isSandwiched(map, x, y, ts, horizontal);
	}

    public static boolean isSandwiched (TerrainEnum map[][], int x, int y, TerrainEnum type[], boolean horizontal) {
    	return (!horizontal && isInMap(map, x - 1, y) && isInMap(map, x + 1, y) && hasMatch(type, map[x - 1][y]) && hasMatch(type, map[x + 1][y]))
				|| (horizontal && isInMap(map, x, y - 1) && isInMap(map, x, y + 1) && hasMatch(type, map[x][y - 1]) && hasMatch(type, map[x][y + 1])	);
	}

	public static boolean hasMatch(TerrainEnum ts[], TerrainEnum type) {
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

	/**
	 * @param map
	 * @param x
	 * @param y
	 * @param type
	 * @return the number of cells in the cardinal orientation that are of type "type".
	 */
    private int getAdjacentTerrainNb(TerrainEnum[][] map, int x , int y, TerrainEnum type) {
      int count = 0;

      if(isInMap(map, x + 1, y) && map[x + 1][y] == type) count ++;
      if(isInMap(map, x - 1, y) && map[x - 1][y] == type) count ++;
      if(isInMap(map, x, y + 1) && map[x][y + 1] == type) count ++;
      if(isInMap(map, x, y - 1) && map[x][y - 1] == type) count ++;

      return count;
    }

	private int getAdjacentTerrainNb(TerrainEnum[][] map, int x , int y, TerrainEnum[] types) {
		int count = 0;

		for(TerrainEnum t : types)
			count += getAdjacentTerrainNb(map, x, y, t);

		return count;
	}

	/**
	 * Says if (x,y) is in map
	 * @param map
	 * @param x
	 * @param y
	 * @return boolean
	 */
    public static boolean isInMap(TerrainEnum[][] map, int x, int y) {
        return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
    }

	/**
	 * Creates a wall of sea all around the map.
	 * @param map
	 * @param size
	 * @return map with a wall of sea.
	 */
    private TerrainEnum[][] surroundBySea(TerrainEnum[][] map, int size) {
        for(int i = 0; i < map.length; i ++)
            for (int j = 0; j < map[0].length; j++)
                if(i <= size || map.length - i - 1 <= size || j <= size || map[0].length - j - 1 <= size)
                    map[i][j] = sea;
        return map;
    }

    @SuppressWarnings("unused")
	private TerrainEnum[][] placeRoads(TerrainEnum[][] map, int nb) {
    	// TODO : placeRoads algorithm.
        while(nb > 0)
            for(int i = 0; i < map.length; i ++)
                for (int j = 0; j < map[0].length; j++) {

                }

        return map;
    }

    public static boolean isInRect(int x, int y, int x0, int y0, int height, int width) {
    	return x >= x0 && y >= y0 && x < x0 + height && y < y0 + width;
	}

	public static boolean isInRect(int x, int y, int height, int width) {
    	return isInRect(x, y, 0, 0, height, width);
	}

	private void setCell(TerrainEnum[][] map, TerrainEnum t, int x, int y) {
    	if(isInMap(map, x, y)) map[x][y] = t;
	}

	private TerrainEnum getCell(TerrainEnum[][] map, int x, int y) {
    	if(isInMap(map, x, y)) return map[x][y];
    	else return none;
	}

	private static boolean isInLayout(AbstractBuilding[][] layout, int x, int y) {
    	return layout != null && isInRect(x, y, layout.length, layout[0].length);
	}

	public static int abs(int i) {
    	return i >= 0 ? i : -i;
	}
}
