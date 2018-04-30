package fr.main.model.generator;

import static fr.main.model.TerrainEnum.beach;
import static fr.main.model.TerrainEnum.bridge;
import static fr.main.model.TerrainEnum.getTerrainEnum;
import static fr.main.model.TerrainEnum.hill;
import static fr.main.model.TerrainEnum.lowland;
import static fr.main.model.TerrainEnum.mLowland;
import static fr.main.model.TerrainEnum.mountain;
import static fr.main.model.TerrainEnum.none;
import static fr.main.model.TerrainEnum.reef;
import static fr.main.model.TerrainEnum.river;
import static fr.main.model.TerrainEnum.road;
import static fr.main.model.TerrainEnum.sea;
import static fr.main.model.TerrainEnum.wood;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import fr.main.model.TerrainEnum;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.Airport;
import fr.main.model.buildings.Barrack;
import fr.main.model.buildings.City;
import fr.main.model.buildings.Dock;
import fr.main.model.buildings.GenericBuilding;
import fr.main.model.buildings.Headquarter;
import fr.main.model.buildings.MissileLauncher;
import fr.main.model.players.Player;

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
    private boolean placeSilo;
    private int cityRingNb;
    private int docksNb;
    private int barracksNb;
    private int airportNb;

    private int buildingCount;

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
        this.starterDock = false;
		this.mountainProportion = 50;
		this.woodProportion = 50;
        buildingCount = 0;
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
        seaBandSize = 3;

    }
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    public void resetSeed() {
        rand = new Random(seed);
        System.out.println("Rand reset to : " + seed);
    }

	public void setPlayers(int playerNb) {
        Player.increment_id = 0;
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
		this.mountainProportion = mountainProportion;
		if(woodProportion + mountainProportion > 100)
			this.mountainProportion = 100 - woodProportion;
	}

	public void setWoodProportion(int woodProportion) {
		if(mountainProportion + woodProportion > 100)
			this.woodProportion = 100 - mountainProportion;
		this.woodProportion = woodProportion;
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
//		else if (seaBandSize > mapHeight / 2) seaBandSize = mapHeight / 2 + 1;
//		else if (seaBandSize > mapWidth / 2) seaBandSize = mapWidth / 2 + 1;
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

	public void setBarracksNb(int barracksNb) {
		this.barracksNb = makeInBound(0, 12, barracksNb);
	}

	public void setAirportNb(int airportNb) {
		this.airportNb = makeInBound(0, 6, airportNb);
	}

	public void setDocksNb(int docksNb) {
		this.docksNb = makeInBound(0, 6, docksNb);
	}

	public void setCityRingNb(int cityRingNb) {
		this.cityRingNb = makeInBound(0, 5, cityRingNb);
	}

	public void setPlaceSilo(boolean placeSilo) {
		this.placeSilo = placeSilo;
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
        terrainPortion[0] = 20; // Lowland 20
        terrainPortion[1] = 30; // Sea 30
        // Somme <= 100

        int[] terrainLeft = new int[2];
        for(int i = 0; i < terrainPortion.length; i ++)
            terrainLeft[i] = referenceNb * terrainPortion[i] / 100;

        /*
        	Places the HQs and other starter buildings, except docks.
        	Also setting the immuable TerrainEnums.
         */

		currentBuildingLayout = placeHeadQuarters(currentBuildingLayout);
		currentBuildingLayout = placeStarterBuildings(currentBuildingLayout);
		currentBuildingLayout = placeBuildings(currentBuildingLayout, currentMap);

		setImmuableTerrain(currentBuildingLayout, currentMap);
		setBuildingCount(currentBuildingLayout);

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

        /*
        	Placing starter Docks.
         */

		currentBuildingLayout = placeStarterDocks(currentMap, currentBuildingLayout);

		/*
			Placing different tile types.
		 */

        placeBeach(currentMap);
        currentMap = placeRivers(currentMap);
        placeMountainsHills(currentMap);
        placeWood(currentMap);

		int[][] nodes = placeRoadNodes(currentMap, currentBuildingLayout);
		placeRoads(currentMap, currentBuildingLayout, nodes);

		/*
			Cleaning the tile map.
		 */

        clean(currentMap);

		/*
			Last operations,
		 */

		lastBuildingLayout = currentBuildingLayout;
        currentBuildingLayout = null;
        lastMap = currentMap;
        currentMap = null;
        lastPlayers = players;

        return lastMap;
    }

	/**
	 * @param layout
	 * Sets the current HQs coordinates.
	 */
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

	/**
	 * Resets currentHQcoordinates to null.
	 */
	public void resetCurrentHQCorrdinates() {
		currentHQCoordinates = null;
	}

	/**
	 * @param min
	 * @param max
	 * @param i
	 * @return i made in bound of [min, max].
	 */
	private int makeInBound(int min, int max, int i) {
		if(i < min) i = min;
		else if (i > max) i = max;
		return i;
	}

	/**
	 * @param array
	 * Prints in the terminal an array of coordinates.
	 */
	private void printCoorArray(int[][] array) {
    	String str = "";
    	for(int[] l : array) {
    		str = "(";
    		for(int i : l)
    			str += i + ",";
			System.out.println(str + ")");
		}
	}

	/**
	 * @param layout
	 * @return building layout with placed starter building except the docks (seperate function)
	 */
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
									new Point(sqrCoor[randNb][1], sqrCoor[randNb][0]));
					break;
				}
			}
			if(starterBarrack && starterBarrack && sqrCoor.length < 2) {
				System.out.println("No place for starter Airport");
			}
			if (starterAirport) {
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
									new Point(sqrCoor[randNb][1], sqrCoor[randNb][0]));
					break;
				}
			}
		}

		return layout;
	}

	/**
	 * @param map
	 * @param layout
	 * @return building layout with placed starter Docks.
	 */
	private AbstractBuilding[][] placeStarterDocks(TerrainEnum[][] map, AbstractBuilding[][] layout) {
		if(!starterDock) return layout;

		int[][] hqs = getCurrentHQCoordinates();
		int randNb;
		for(int[] coor : hqs) {
			// Find nearest sea tile.
			int[][] nearest = findNearestTerrainEnums(map, coor[0], coor[1], sea);
			for(int i = 0; i < 30; i ++) {
				randNb = rand.nextInt(nearest.length);
				if (nearest[randNb] == null) {
					System.out.println("sqr[" + randNb + "] null.");
					if(i == 29)
						System.out.println("No valid docks");
					continue;
				}
				if (!(layout[nearest[randNb][0]][nearest[randNb][1]] == null)) {
					if(i == 29)
						System.out.println("No valid docks");
					continue;
				}
				if(starterDock)
					layout[nearest[randNb][0]][nearest[randNb][1]] =
						new Dock(
								((Headquarter) layout[coor[0]][coor[1]]).getOwner(),
								new Point(nearest[randNb][1], nearest[randNb][0]));
				else
					layout[nearest[randNb][0]][nearest[randNb][1]] =
							new Dock(
									null,
									new Point(nearest[randNb][1], nearest[randNb][0]));
				break;
			}
		}

		return layout;
	}

	/**
	 * @param mapHeight
	 * @param mapWidth
	 * @return building layout with placed HQs.
	 */
	private AbstractBuilding[][] placeHeadQuarters(int mapHeight, int mapWidth) {
    	AbstractBuilding[][] layout = new AbstractBuilding[mapHeight][mapWidth];
		for(int i = 0; i < mapHeight; i ++)
			for(int j = 0; j < mapWidth; j ++)
				layout[i][j] = new GenericBuilding(i, j);

		return placeHeadQuarters(layout);
    }

	/**
	 * @param layout
	 * @return building layout with placed HQs.
	 */
	private AbstractBuilding[][] placeHeadQuarters(AbstractBuilding[][] layout) {
    	int x = layout.length,
				y = layout[0].length;
    	int randNb = rand.nextInt(3);
    	int realX = x - 2 * seaBandSize - 4 - 2 * randNb,
				realY = y - 2 * seaBandSize - 4 - 2 * randNb;
		boolean useWidth = x < y;

		System.out.println("realX : " + realX + "\nrealY :" + realY);

//		AbstractBuilding[] rect = getRectangleFromSide(layout, seaBandSize + 1 + randNb);

		AbstractBuilding[] rect = getRectangle(layout, x/2, y/2,
				x/2 - seaBandSize - randNb - 2, y/2 - seaBandSize - randNb - 2);
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
				l.add(realY + realX);
				l.add(2 * realY + realX);
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
			layout[rect[hqs.get(i)].getX()][rect[hqs.get(i)].getY()] = new Headquarter(players[i], new Point(rect[hqs.get(i)].getY(), rect[hqs.get(i)].getX()));
			currentHQCoordinates[i][0] = rect[hqs.get(i)].getX();
			currentHQCoordinates[i][1] = rect[hqs.get(i)].getY();
		}

		System.out.println("HQs placed !");
		return layout;
	}

	/**
	 * @param layout
	 * @param distanceFromSide
	 * @return an array of Abstract building on the tiles distant of distanceFromSide for each side.
	 */
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
		System.out.println("Size of rectFormSide :" + rectangle.length);
		return rectangle;
	}

	/**
	 * @param layout
	 * @param x0
	 * @param y0
	 * @return array of AbstractBuildings on the tiles on the side of a square of size
	 * 3 and of center (x0, y0).
	 */
	private AbstractBuilding[] getSquare(AbstractBuilding[][] layout, int x0, int y0) {
		return getSquare(layout, x0, y0, 1);
	}

	/**
	 * @param layout
	 * @param x0
	 * @param y0
	 * @param dist
	 * @return array of AbstractBuildings on the tiles on the side of a square of size
	 * dist * 2 + 1 and of center (x0, y0).
	 */
	private AbstractBuilding[] getSquare(AbstractBuilding[][] layout, int x0, int y0, int dist) {
    	return getRectangle(layout, x0, y0, dist, dist);
	}

	/**
	 * @param layout
	 * @param x0
	 * @param y0
	 * @param distH
	 * @param distW
	 * @return array of AbstractBuildings on the tiles on the side of a rectangle of size
	 * (distH * 2 + 1)x(distW * 2 + 1) and of center (x0, y0).
	 */
	private AbstractBuilding[] getRectangle(AbstractBuilding[][] layout, int x0, int y0, int distH, int distW) {
    	if(layout == null) return new AbstractBuilding[0];
    	return coordinatesToAbstractBuilding(getRectangle(layout.length, layout[0].length, x0, y0, distH, distW), layout);
	}

	/**
	 * @param height
	 * @param width
	 * @param x0
	 * @param y0
	 * @return coordinates of tiles on the sides of a square of center (x0, y0)
	 * and of size 3.
	 */
	private int[][] getSquare(int height, int width, int x0, int y0) {
    	return getSquare(height, width, x0, y0, 1);
	}

	/**
	 * @param height
	 * @param width
	 * @param x0
	 * @param y0
	 * @param dist
	 * @return coordinates of tiles on the sides of a square of center (x0, y0)
	 * and of size dist * 2 + 1.
	 */
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

		return  coorArrayFromLinkedList(list);
	}

	/**
	 * @param height
	 * @param width
	 * @param x0
	 * @param y0
	 * @param distFromCenter
	 * @return tiles on the sides of a diamond of center (x0, y0)
	 * and diagonals of length distFromCenter * 2.
	 */
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
				item = new int[2];
				item[0] = last[i][0] - 1;
				item[1] = last[i][1];
				coors.add(item);
			}
			if (last[i][0] >= x0 && isInRect(last[i][0] + 1, last[i][1], height, width)) {
				item = new int[2];
				item[0] = last[i][0] + 1;
				item[1] = last[i][1];
				coors.add(item);
			}
			if (last[i][0] == x0 && last[i][1] >= y0 && isInRect(x0, last[i][1] + 1, height, width)) {
				item = new int[2];
				item[0] = x0;
				item[1] = last[i][1] + 1;
				coors.add(item);
			}
			if (last[i][0] == x0 && last[i][1] <= y0 && isInRect(x0, last[i][1] - 1, height, width)) {
				item = new int[2];
				item[0] = x0;
				item[1] = last[i][1] - 1;
				coors.add(item);
			}
		}

		return coorArrayFromLinkedList(coors);
	}

	/**
	 * Sets buildingCount variable.
	 * @param layout
	 * @return building count.
	 */
	private int setBuildingCount(AbstractBuilding[][] layout) {
		int i = 0;

		for(AbstractBuilding[] bs : layout)
			for(AbstractBuilding b : bs)
				if(isNonGenericBuilding(b))
					i ++;
		return i;
	}

	/**
	 * @param coors
	 * @param layout
	 * @return an array of building from coordinates.
	 */
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

	/**
	 * @param map
	 * @param x
	 * @param y
	 * @param type
	 * @return coordinates to the nearest TerrainEnums of type 'type'.
	 */
	private int[][] findNearestTerrainEnums(TerrainEnum[][] map, int x, int y, TerrainEnum type) {
		TerrainEnum[] types = {type};
		return findNearestTerrainEnums(map, x, y, types);
	}

	/**
	 * @param map
	 * @param x
	 * @param y
	 * @param types
	 * @return coordinates to the nearest TerrainEnums of type from 'types'.
	 */
	private int[][] findNearestTerrainEnums(TerrainEnum[][] map, int x, int y, TerrainEnum[] types) {
		int[][] dmnd;
		int[][] out;
		int size = 1;
		LinkedList<int[]> found = new LinkedList<>();

		label:
		while (true) {
			dmnd = getDiamond(map.length, map[0].length, x, y, size);
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

		return coorArrayFromLinkedList(found);
	}

	/**
	 * @param height
	 * @param width
	 * @param x
	 * @param y
	 * @return the coordinates of the tiles adjacent (in a cross) to tile (x, y).
	 */
	public static int[][] getCross(int height, int width, int x, int y) {
		LinkedList<int[]> list = new LinkedList<>();
		int[] item;

		if(isInRect(x + 1, y, height, width)) {
			item = new int[2];
			item[0] = x + 1;
			item[1] = y;
			list.add(item);
		}
		if(isInRect(x - 1, y, height, width)) {
			item = new int[2];
			item[0] = x - 1;
			item[1] = y;
			list.add(item);
		}
		if(isInRect(x, y + 1, height, width)) {
			item = new int[2];
			item[0] = x;
			item[1] = y + 1;
			list.add(item);
		}
		if(isInRect(x, y - 1, height, width)) {
			item = new int[2];
			item[0] = x;
			item[1] = y - 1;
			list.add(item);
		}

		return coorArrayFromLinkedList(list);
	}

	private int[][] getCross(int x, int y) {
		return getCross(currentMap.length, currentMap[0].length, x, y);
	}

	/**
	 * @param list
	 * @return an array of coordinates from a LinkedList of coordinates.
	 */
	public static int[][] coorArrayFromLinkedList(LinkedList<int[]> list) {
		int[][] out = new int[list.size()][2];

		for(int i = 0; i < list.size(); i ++) {
			out[i][0] = list.get(i)[0];
			out[i][1] = list.get(i)[1];
		}

		return out;
	}

	/**
	 * @param layout of buildings.
	 * @param map of tiles.
	 * @return map with setted mLocations that will remain unchanged until the end of
	 * the generation of the map.
	 */
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


	/**
	 * @param map
	 * @return map with all mLocation transformed to lowland tiles.
	 */
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
            if(dist == 0) continue;
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

	private TerrainEnum[][] refineMap(TerrainEnum[][] map) {
        return refineMap(map, 1);
    }

	/**
	 * @param map
	 * @return map of tiles after being refined by this cellular automaton.
	 */
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

	/**
	 * @param map
	 * @return map of tiles with placed beach tiles.
	 */
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

	/**
	 * @param layout of buildings.
	 * @param map of tiles.
	 * @return building layout with placed buildings.
	 */
	private AbstractBuilding[][] placeBuildings(AbstractBuilding[][] layout, TerrainEnum[][] map) {
    	LinkedList<AbstractBuilding> bs;

		System.out.println("Cities");
		for(int i = 0; i < cityRingNb; i ++) {
    		bs = new LinkedList<>();
    		for(int j = 0; j < players.length; j ++)
    			bs.add(new City(null, null));
			layout = placeBuildingRing(layout, map, bs, rand.nextInt(layout.length - 2 * seaBandSize - 2));
		}

		System.out.println("Barracks");
		int barracks = barracksNb;
		while(barracks > 0) {
			bs = new LinkedList<>();
			for(int i = 0; i < 4 && barracks > 0; i ++) {
				bs.add(new Barrack(null, null));
				barracks --;
			}
			layout = placeBuildingRing(layout, map, bs, rand.nextInt(layout.length - 2 * seaBandSize - 2));
		}

		System.out.println("Docks");
		int docks = docksNb;
		while(docks > 0) {
			bs = new LinkedList<>();
			for(int i = 0; i < 4 && docks > 0; i ++) {
				bs.add(new Dock(null, null));
				docks --;
			}
			layout = placeBuildingRing(layout, map, bs, rand.nextInt(layout.length - 2 * seaBandSize - 2));
		}

		System.out.println("Airports");
		int airports = airportNb;
		while(airports > 0) {
			bs = new LinkedList<>();
			for(int i = 0; i < 4 && airports > 0; i ++) {
				bs.add(new Airport(null, null));
				airports --;
			}
			layout = placeBuildingRing(layout, map, bs, rand.nextInt(layout.length - 2 * seaBandSize - 2));
		}

		System.out.println("Silo");
		if(placeSilo) {
			layout[layout.length / 2][layout[0].length / 2] = new MissileLauncher(new Point(layout[0].length / 2, layout.length / 2));
		}

		return layout;
	}

	/**
	 * @param layout of buildings.
	 * @param map of tiles.
	 * @param buildings to place.
	 * @param distFromCenter of the sides of the rectangle.
	 * @return layout with placed buildings on a rectangle.
	 */
	private AbstractBuilding[][] placeBuildingRing(AbstractBuilding[][] layout, TerrainEnum[][] map , LinkedList<AbstractBuilding> buildings, int distFromCenter) {
    	if(buildings.size() == 0) {
    		System.out.println("Building array size is 0");
    		System.exit(500);
    		return layout;
		}

    	int h = layout.length;
    	int w = layout[0].length;
    	boolean useH = h > w;
    	float proportion = useH ? h / w : w / h;

		if(distFromCenter > h/2 - 2 * seaBandSize - 2)
			distFromCenter = h/2 - 2 * seaBandSize - 2;
		if(distFromCenter > w/2 - 2 * seaBandSize - 2)
			distFromCenter = w/2 - 2 * seaBandSize - 2;

		int distH = (int)(useH ? (float) distFromCenter * proportion : distFromCenter);
		int distW = (int)(useH ? distFromCenter : (float) distFromCenter * proportion);


		int[][] rect = getRectangle(h, w,h/2,w/2, distH, distW);

		float step = (float) rect.length / (float) buildings.size();

		int n = rand.nextInt(rect.length);

		for(AbstractBuilding b : buildings) {
			if(isNonGenericBuilding(layout[rect[n][0]][rect[n][1]]))
				n = (n + 1) % rect.length;

			b.setLocation(new Point(rect[n][1], rect[n][0]));
			layout[rect[n][0]][rect[n][1]] = b;

			// If Dock, surround with water.
			if(b instanceof Dock) {
				int[][] cross = getCross(rect[n][1], rect[n][0]);
				int randNb = rand.nextInt(cross.length);

				for(int i = 0; i < cross.length; i ++)
					if(i != randNb) {
						map[cross[i][0]][cross[i][1]] = sea;
					}
			}

			n = (int) ((float) n + step) % rect.length;
		}

		return layout;
	}


	/**
	 * @param map
	 * @return map with placed mountain and hill tiles.
	 */
    private TerrainEnum[][] placeMountainsHills(TerrainEnum[][] map) {
		if(mountainProportion == 0) return map;
        int mountains = 0;
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == lowland && getAdjacentTerrainNb(map, i, j, bridge) == 0) {
                    mountains = getAdjacentTerrainNb(map, i, j, mountain);
                    if(mountains == 0 && rand.nextInt(20 - (int)((float) mountainProportion / 5.55)) < 1) map[i][j] = mountain;
                    else if(mountains > 0 && rand.nextInt(15) < 4) map[i][j] = mountain;
                    else if(mountains == 0 && rand.nextInt(12) < 2) map[i][j] = hill;
                    else if((mountains > 0 || getAdjacentTerrainNb(map, i, j, hill) > 0)
                            && rand.nextInt(12) < 3) map[i][j] = hill;
                }
            }
        return map;
    }

	/**
	 * @param map
	 * @return map with placed wood tiles.
	 */
	private TerrainEnum[][] placeWood(TerrainEnum[][] map) {
    	if(woodProportion == 0) return map;
        for(int i = 0; i < map.length; i ++)
            for(int j = 0; j < map[0].length; j ++) {
                if(map[i][j] == lowland || map[i][j] == hill)
                    if(getAdjacentTerrainNb(map, i, j, wood) == 0)
                        map[i][j] = (rand.nextInt(20 - (int)((float) woodProportion / 5.55)) < 2) ? wood : map[i][j];
                    else map[i][j] = (rand.nextInt(10 - (int)((float) woodProportion / 11.1)) < 2) ? wood: map[i][j];
            }
        return map;
    }

	/**
	 * @param b
	 * @return true if b is not null and not a GenericBuilding.
	 */
	public static boolean isNonGenericBuilding(AbstractBuilding b) {
    	return b != null && !(b instanceof GenericBuilding);
	}

	/**
	 * @param map to clean.
	 * @param it number of iterations.
	 * @return cleaned map.
	 */
    private TerrainEnum[][] clean(TerrainEnum[][] map, int it) {
    	resetImmuableTerrain(map);
    	for(int i = 0; i < it; i ++)
    		map = clean(map);
    	return map;
	}

	/**
	 * @param layout to clean.
	 * @return layout with last modifications left from the different gen algorithms.
	 */
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

	/**
	 * @param map to clean.
	 * @return map with last modifications left from the different gen algorithms.
	 */
	private TerrainEnum[][] clean (TerrainEnum[][] map) {
		TerrainEnum[] naval = {reef, sea, river};
		TerrainEnum[] land = {wood, lowland, mLowland, mountain, hill, road};

		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++) {
				switch (map[i][j]) {
					case lowland:
						map = cleanLowland(map, i, j);
					case beach:
						if (getAdjacentTerrainNb(map, i, j, sea) >= 3 && getAdjacentTerrainNb(map, i, j, land) == 0)
							map[i][j] = sea;
						else if (getAdjacentTerrainNb(map, i, j, beach) < 3)
							map[i][j] = lowland;
						break;
					case sea:
						if(getAdjacentTerrainNb(map, i, j, sea) == 1)
							map[i][j] = river;
						break;
					case river:
						if(getAdjacentTerrainNb(map, i, j, sea) + getAdjacentTerrainNb(map, i, j, river) == 0)
							map[i][j] = wood;
						break;
					case road:
						if(isSandwiched(map, i, j, naval)
								&& getAdjacentTerrainNb(map, i, j, naval) == 2)
							map[i][j] = bridge;

						int[][] sqr = getFilledSquare(map.length, map[0].length, i, j, 2);
						boolean clean = sqr.length == 0 ? true : false;

						for(int[] coor : sqr)
							if(map[coor[0]][coor[1]] != road)
								clean = true;

						if(!clean) {
							LinkedList<Integer> remove = new LinkedList<>();
							for (int k = 0; k < sqr.length; k++) {
								if (getAdjacentTerrainNb(map, sqr[k][0], sqr[k][1], road)
										+ getAdjacentTerrainNb(map, sqr[k][0], sqr[k][1], bridge) < 3)
								remove.add(k);
							}
							for (int k : remove) {
								map[sqr[k][0]][sqr[k][1]] = lowland;
							}
						}
						break;
					case bridge:
						if(getAdjacentTerrainNb(map, i, j, naval) >= 3)
							map[i][j] = sea;
						// TODO : put every cleaning procedure here.
				}
				if(isNonGenericBuilding(currentBuildingLayout[i][j]))
					map[i][j] = lowland;
			}

		map = placeRivers(map);

		return map;
	}

	/**
	 * @param map
	 * @param x
	 * @param y
	 * @return Cleaned map from tile (x, y).
	 */
	private TerrainEnum[][] cleanLowland(TerrainEnum[][] map, int x, int y) {
		TerrainEnum[] land = {mountain, lowland, mLowland, hill, wood, road};
		if(isInMap(map, x + 2, y) && map[x + 2][y] == sea && isInMap(map, x + 1, y) && map[x + 1][y] == sea) {
			if(isInMap(map, x, y + 1) && map[x][y + 1] == sea
					&& isInMap(map, x + 2, y + 1) && hasMatch(land, map[x + 2][y + 1])
					&& isInMap(map, x + 1, y + 1) && map[x + 1][y + 1] == sea)
				map[x + 2][y + 1] = sea;
			if(isInMap(map, x, y - 1) && map[x][y - 1] == sea
					&& isInMap(map, x + 2, y - 1) && hasMatch(land, map[x + 2][y - 1])
					&& isInMap(map, x + 1, y - 1) && map[x + 1][y - 1] == sea)
				map[x + 2][y - 1] = sea;
		}

		return map;
	}


	/**
	 * @param mapHeight
	 * @param mapWidth
	 * @param x0 of top left corner.
	 * @param y0 of top left corner.
	 * @param size or square side.
	 * @return the coordinates of tiles from a square.
	 */
	private int[][] getFilledSquare(int mapHeight, int mapWidth, int x0, int y0, int size) {
    	LinkedList<int[]> out = new LinkedList<>();

    	if(!isInRect(x0, y0, mapHeight, mapWidth))
    		return new int[0][0];

    	int[] coor;

    	for(int i = x0; i < size + x0; i++)
    		for(int j = y0; j < size + y0; j ++)
    			if(isInRect(i, j, mapHeight, mapWidth)) {
    				coor = new int[2];
    				coor[0] = i;
    				coor[1] = j;
    				out.add(coor);
				}

		return coorArrayFromLinkedList(out);
	}

	/**
	 * @param a
	 * @param b
	 * @return true if a is b
	 */
	private boolean isSame(int[] a, int[] b) {
    	return a[0] == b[0] && a[1] == b[1];
	}

	/**
	 * @param map
	 * @return map with placed river tiles.
	 */
	private TerrainEnum[][] placeRivers (TerrainEnum[][] map) {
		TerrainEnum[] land = {lowland, bridge, mountain, hill, wood}, naval = {none, sea, river};

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


	/**
	 * @param map
	 * @param x
	 * @param y
	 * @return tiles of a square of size 3 with (x, y) as center.
	 */
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

	private int[][] placeRoadNodes(TerrainEnum[][] map, AbstractBuilding[][] layout) {
		AbstractBuilding b;
		int randNb;
		int[][] cross;
		LinkedList<int[]> list = new LinkedList<>();

		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j++) {
				b = layout[i][j];
				if(isNonGenericBuilding(b)) {
					if(b instanceof Headquarter || rand.nextInt(10) >= 2) {
						cross = getCross(i, j);
						for (int k = 0; k < 30; k ++) {
							randNb = rand.nextInt(cross.length);
							if(isNonGenericBuilding(layout[cross[randNb][0]][cross[randNb][1]])
									|| map[cross[randNb][0]][cross[randNb][1]] == sea
									|| map[cross[randNb][0]][cross[randNb][1]] == reef)
								continue;
							map[cross[randNb][0]][cross[randNb][1]] = road;
							list.add(cross[randNb]);
							break;
						}
					}
				}
			}

		return coorArrayFromLinkedList(list);
	}

	/**
	 * @param map
	 * @param layout
	 * @param roadNodes
	 * @return map with placed road tiles.
	 */
	private TerrainEnum[][] placeRoads(TerrainEnum[][] map, AbstractBuilding[][] layout, int[][] roadNodes) {
		int[][] weights = getWeights(map);
		boolean[][] blocks = getBlocks(layout);

		AStar aStar = new AStar(weights, blocks);

		int[][] road;
		int randNb;

		for(int[] coor : roadNodes) {
//			int p = closestPoint(coor[0], coor[1], roadNodes);
			aStar.setWeights(getWeights(map));
			randNb = rand.nextInt(roadNodes.length);
			road = aStar.shortestRoute(coor[0], coor[1], roadNodes[randNb][0], roadNodes[randNb][1]);
			System.out.println("Road length : " + road.length);
			map = placeRoads(map, road);
		}

        return map;
    }

    private TerrainEnum[][] placeRoads(TerrainEnum[][] map, int[][] roadPath) {
    	for(int[] coor : roadPath) {
    		map[coor[0]][coor[1]] = road;
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

	/**
	 * @param map
	 * @return an array of array of weights for the pathfinding algo for the roads placement.
	 */
	private int[][] getWeights(TerrainEnum[][] map) {
		int[][] weights = new int[map.length][map[0].length];

		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++) {
				if(map[i][j] == sea || map[i][j] == reef)
					weights[i][j] = 50;
				else if(map[i][j] == mountain)
					weights[i][j] = 40;
				else if(map[i][j] == hill)
					weights[i][j] = 30;
				else if(map[i][j] == wood || map[i][j] == beach)
					weights[i][j] = 25;
				else if(map[i][j] == road || map[i][j] == bridge)
					weights[i][j] = 1;
				else
					weights[i][j] = 10;
			}
		return weights;
	}

	/**
	 * @param layout
	 * @return an array of array of boolean reprensenting if a can be a road or not
	 * for the pathfinding algo for the roads placement.
	 */
	private boolean[][] getBlocks(AbstractBuilding[][] layout) {
    	boolean[][] blocks = new boolean[layout.length][layout[0].length];
    	for(int i = 0; i < layout.length; i ++)
			for(int j = 0; j < layout[0].length; j ++)
    			if(layout[i][j] != null && !(layout[i][j] instanceof GenericBuilding))
					blocks[i][j] = true;
    	return blocks;
	}
}
