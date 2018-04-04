package fr.main.model;

import java.awt.Point;

/**
 * A move zone is a part of a map on which an unit can go, with differents informations about the tiles
 */
public class MoveZone {

	public final Node[][] map;
	public final Point offset;

	public MoveZone(Node[][] n, Point offset){
		this.map    = n;
		this.offset = offset;
	}
}